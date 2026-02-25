package ham.hamcrawler.engine;

import java.net.URI;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ham.hamcrawler.model.CrawlCallbacks;
import ham.hamcrawler.model.CrawlMode;
import ham.hamcrawler.model.CrawlOptions;
import ham.hamcrawler.model.CrawlStatus;

public class CrawlerEngine {
    private static final long IDLE_BACKOFF_MS = 25;

    private final FetchService fetchService;
    private final LinkExtractor linkExtractor;

    private final ConcurrentLinkedQueue<String> queue;
    private final Set<String> visited;
    private final Set<String> scheduled;
    private final Map<String, RobotsRules> robotsRulesCache;

    private ExecutorService executor;
    private AtomicBoolean running;
    private AtomicBoolean stopRequested;
    private AtomicInteger activeWorkers;
    private AtomicInteger discoveredCount;
    private AtomicInteger pendingUrls;
    private AtomicReference<String> lastUrl;

    public CrawlerEngine() {
        this.fetchService = new FetchService();
        this.linkExtractor = new LinkExtractor();
        this.queue = new ConcurrentLinkedQueue<>();
        this.visited = ConcurrentHashMap.newKeySet();
        this.scheduled = ConcurrentHashMap.newKeySet();
        this.robotsRulesCache = new ConcurrentHashMap<>();
        this.running = new AtomicBoolean(false);
        this.stopRequested = new AtomicBoolean(false);
        this.activeWorkers = new AtomicInteger(0);
        this.discoveredCount = new AtomicInteger(0);
        this.pendingUrls = new AtomicInteger(0);
        this.lastUrl = new AtomicReference<>(null);
    }

    public synchronized boolean isRunning() {
        return running.get();
    }

    public synchronized void stop() {
        stopRequested.set(true);
        running.set(false);
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public synchronized void start(CrawlOptions options, CrawlCallbacks callbacks) {
        if (running.get()) {
            callbacks.onError("Crawler is already running.", null);
            return;
        }

        clearState();
        running.set(true);
        stopRequested.set(false);

        String normalizedStartUrl = normalizeUrl(options.startUrl());
        if (normalizedStartUrl == null) {
            callbacks.onError("Invalid start URL.", null);
            running.set(false);
            return;
        }

        int threadCount = Math.max(1, options.threadCount());
        queue.offer(normalizedStartUrl);
        scheduled.add(normalizedStartUrl);
        discoveredCount.incrementAndGet();
        pendingUrls.set(1);
        HamBugLogger.log("[HAM] Crawl started | url=" + normalizedStartUrl + " | mode=" + options.mode() + " | threads=" + threadCount);

        callbacks.onStarted(options);

        AtomicInteger threadIndex = new AtomicInteger(1);
        ThreadFactory workerFactory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("HamBug-" + threadIndex.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        };

        executor = Executors.newFixedThreadPool(threadCount, workerFactory);
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> workerLoop(options, callbacks));
        }

        Thread completionWatcher = new Thread(() -> {
            try {
                executor.shutdown();
                boolean terminated = executor.awaitTermination(7, TimeUnit.DAYS);
                if (!terminated) {
                    HamBugLogger.log("[HAM] Crawl termination timeout while waiting for workers.");
                    callbacks.onError("Crawler termination timeout.", null);
                }
                finalizeRun(callbacks);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                HamBugLogger.log("[HAM] Crawl interrupted while waiting for completion.");
                callbacks.onError("Crawler interrupted while waiting for completion.", exception);
                finalizeRun(callbacks);
            }
        }, "ham-crawler-completion-watcher");
        completionWatcher.setDaemon(true);
        completionWatcher.start();
    }

    private void workerLoop(CrawlOptions options, CrawlCallbacks callbacks) {
        URI startUri = linkExtractor.toUri(normalizeUrl(options.startUrl()));

        while (running.get()) {
            String nextUrl = normalizeUrl(queue.poll());
            if (nextUrl == null) {
                if (pendingUrls.get() == 0) {
                    running.set(false);
                    break;
                }

                sleepQuietly();
                continue;
            }

            if (!visited.add(nextUrl)) {
                pendingUrls.decrementAndGet();
                continue;
            }

            String workerName = Thread.currentThread().getName();
            activeWorkers.incrementAndGet();
            lastUrl.set(nextUrl);
            callbacks.onProgress(snapshot(nextUrl));
            try {
                if (!options.extractNonHtml() && looksLikeNonHtmlAsset(nextUrl)) {
                    continue;
                }

                if (!isAllowedByRobots(nextUrl, options)) {
                    HamBugLogger.log("[" + workerName + "] skipped-robots=" + nextUrl);
                    continue;
                }

                Document document = fetchService.fetch(nextUrl);
                if (document == null) {
                    continue;
                }

                String pageTitle = document.title();
                if (pageTitle == null || pageTitle.isBlank()) {
                    pageTitle = "(untitled page)";
                }
                HamBugLogger.log("[" + workerName + "] visited=" + nextUrl + " | title=" + pageTitle);

                if (options.extractMetadata()) {
                    HamBugLogger.log("[" + workerName + "] metadata=" + extractMetadataSummary(document) + " | page=" + nextUrl);
                }

                Set<String> discoveredUrls = linkExtractor.extractLinks(document, nextUrl);
                HamBugLogger.log("[" + workerName + "] discovered=" + discoveredUrls.size() + " links/assets | page=" + nextUrl);

                for (String discoveredUrl : discoveredUrls) {
                    String canonicalDiscoveredUrl = normalizeUrl(discoveredUrl);
                    if (canonicalDiscoveredUrl == null) {
                        continue;
                    }

                    if (!isAllowedByMode(startUri, canonicalDiscoveredUrl, options.mode())) {
                        continue;
                    }

                    if (!shouldQueueDiscoveredUrl(canonicalDiscoveredUrl, options)) {
                        continue;
                    }

                    if (scheduled.add(canonicalDiscoveredUrl)) {
                        queue.offer(canonicalDiscoveredUrl);
                        pendingUrls.incrementAndGet();
                        discoveredCount.incrementAndGet();
                        HamBugLogger.log("[" + workerName + "] found=" + canonicalDiscoveredUrl + " | from=" + nextUrl);
                    }
                }
            } catch (Exception exception) {
                HamBugLogger.log("[" + workerName + "] error=" + nextUrl + " | message=" + exception.getMessage());
                callbacks.onError("Error crawling: " + nextUrl, exception);
            } finally {
                activeWorkers.decrementAndGet();
                pendingUrls.decrementAndGet();
                callbacks.onProgress(snapshot(nextUrl));
            }
        }
    }

    private boolean isAllowedByMode(URI startUri, String candidateUrl, CrawlMode mode) {
        if (mode == CrawlMode.SEED) {
            return true;
        }

        URI candidateUri = linkExtractor.toUri(candidateUrl);
        if (startUri == null || candidateUri == null) {
            return false;
        }

        String startHost = startUri.getHost();
        String candidateHost = candidateUri.getHost();
        return startHost != null && startHost.equalsIgnoreCase(candidateHost);
    }

    private boolean shouldQueueDiscoveredUrl(String discoveredUrl, CrawlOptions options) {
        if (options.extractNonHtml()) {
            return true;
        }

        return !looksLikeNonHtmlAsset(discoveredUrl);
    }

    private boolean looksLikeNonHtmlAsset(String discoveredUrl) {
        URI uri = linkExtractor.toUri(discoveredUrl);
        if (uri == null) {
            return false;
        }

        String path = uri.getPath();
        if (path == null || path.isBlank() || path.endsWith("/")) {
            return false;
        }

        int dotIndex = path.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == path.length() - 1) {
            return false;
        }

        String extension = path.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
        return switch (extension) {
            case "pdf", "jpg", "jpeg", "png", "gif", "webp", "svg", "ico", "bmp",
                    "mp3", "wav", "ogg", "m4a", "mp4", "webm", "mov", "avi",
                    "css", "js", "map", "woff", "woff2", "ttf", "otf", "eot",
                    "zip", "rar", "7z", "tar", "gz", "tgz", "exe", "dmg", "pkg",
                    "doc", "docx", "xls", "xlsx", "ppt", "pptx", "csv", "json", "xml" -> true;
            default -> false;
        };
    }

    private void finalizeRun(CrawlCallbacks callbacks) {
        CrawlStatus finalStatus = snapshot(lastUrl.get());
        boolean stoppedByUser = stopRequested.get();

        clearExecutionRefs();

        if (stoppedByUser) {
            HamBugLogger.log("[HAM] Crawl stopped | visited=" + finalStatus.visitedCount() + " | discovered=" + finalStatus.discoveredCount() + " | queued=" + finalStatus.queuedCount());
            callbacks.onStopped(finalStatus);
        } else {
            HamBugLogger.log("[HAM] Crawl completed | visited=" + finalStatus.visitedCount() + " | discovered=" + finalStatus.discoveredCount() + " | queued=" + finalStatus.queuedCount());
            callbacks.onCompleted(finalStatus);
        }
    }

    private String extractMetadataSummary(Document document) {
        String description = getMeta(document, "description");
        String keywords = getMeta(document, "keywords");
        String ogTitle = getMeta(document, "og:title");
        String ogDescription = getMeta(document, "og:description");
        String canonical = getCanonical(document);
        String lang = document.select("html").attr("lang");

        return "description=" + safeMetadataValue(description)
                + " | keywords=" + safeMetadataValue(keywords)
                + " | og:title=" + safeMetadataValue(ogTitle)
                + " | og:description=" + safeMetadataValue(ogDescription)
                + " | canonical=" + safeMetadataValue(canonical)
                + " | lang=" + safeMetadataValue(lang);
    }

    private String getMeta(Document document, String key) {
        Element byName = document.selectFirst("meta[name='" + key + "']");
        if (byName != null) {
            return byName.attr("content");
        }

        Element byProperty = document.selectFirst("meta[property='" + key + "']");
        if (byProperty != null) {
            return byProperty.attr("content");
        }

        return null;
    }

    private String getCanonical(Document document) {
        Element canonical = document.selectFirst("link[rel='canonical']");
        if (canonical == null) {
            return null;
        }

        String href = canonical.absUrl("href");
        if (href == null || href.isBlank()) {
            href = canonical.attr("href");
        }
        return href;
    }

    private String safeMetadataValue(String value) {
        if (value == null || value.isBlank()) {
            return "<none>";
        }

        String normalized = value.replaceAll("\\s+", " ").trim();
        if (normalized.length() > 120) {
            return normalized.substring(0, 120) + "...";
        }
        return normalized;
    }

    private String normalizeUrl(String rawUrl) {
        URI uri = linkExtractor.toUri(rawUrl);
        if (uri == null) {
            return null;
        }
        return linkExtractor.normalize(uri);
    }

    private boolean isAllowedByRobots(String url, CrawlOptions options) {
        if (!options.respectRobots()) {
            return true;
        }

        URI uri = linkExtractor.toUri(url);
        if (uri == null) {
            return true;
        }

        String origin = getOrigin(uri);
        if (origin == null) {
            return true;
        }

        RobotsRules rules = robotsRulesCache.computeIfAbsent(origin, this::loadRobotsRules);
        return rules.isAllowed(uri.getPath());
    }

    private String getOrigin(URI uri) {
        String host = uri.getHost();
        String scheme = uri.getScheme();
        if (host == null || scheme == null) {
            return null;
        }

        int port = uri.getPort();
        if (port > -1) {
            return scheme + "://" + host + ":" + port;
        }
        return scheme + "://" + host;
    }

    private RobotsRules loadRobotsRules(String origin) {
        try {
            Connection.Response response = Jsoup.connect(origin + "/robots.txt")
                    .userAgent("FetchHam/1.0")
                    .timeout(10_000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true)
                    .execute();

            if (response.statusCode() >= 400) {
                return RobotsRules.allowAll();
            }

            return RobotsRules.parse(response.body());
        } catch (Exception exception) {
            return RobotsRules.allowAll();
        }
    }

    private static class RobotsRules {
        private final Set<String> allowPaths;
        private final Set<String> disallowPaths;

        private RobotsRules(Set<String> allowPaths, Set<String> disallowPaths) {
            this.allowPaths = allowPaths;
            this.disallowPaths = disallowPaths;
        }

        static RobotsRules allowAll() {
            return new RobotsRules(Set.of(), Set.of());
        }

        static RobotsRules parse(String robotsText) {
            if (robotsText == null || robotsText.isBlank()) {
                return allowAll();
            }

            Set<String> allow = new HashSet<>();
            Set<String> disallow = new HashSet<>();
            boolean applies = false;

            for (String rawLine : robotsText.split("\\R")) {
                String line = stripComment(rawLine).trim();
                if (line.isBlank()) {
                    continue;
                }

                int separatorIndex = line.indexOf(':');
                if (separatorIndex <= 0) {
                    continue;
                }

                String key = line.substring(0, separatorIndex).trim().toLowerCase(Locale.ROOT);
                String value = line.substring(separatorIndex + 1).trim();

                if ("user-agent".equals(key)) {
                    applies = "*".equals(value);
                    continue;
                }

                if (!applies) {
                    continue;
                }

                if ("allow".equals(key)) {
                    String rule = normalizeRule(value);
                    if (rule != null) {
                        allow.add(rule);
                    }
                } else if ("disallow".equals(key)) {
                    String rule = normalizeRule(value);
                    if (rule != null) {
                        disallow.add(rule);
                    }
                }
            }

            return new RobotsRules(allow, disallow);
        }

        boolean isAllowed(String rawPath) {
            String path = (rawPath == null || rawPath.isBlank()) ? "/" : rawPath;

            String winningAllow = longestMatching(path, allowPaths);
            String winningDisallow = longestMatching(path, disallowPaths);

            if (winningAllow == null && winningDisallow == null) {
                return true;
            }
            if (winningAllow == null) {
                return false;
            }
            if (winningDisallow == null) {
                return true;
            }

            return winningAllow.length() >= winningDisallow.length();
        }

        private static String longestMatching(String path, Set<String> rules) {
            String best = null;
            for (String rule : rules) {
                if (rule.isEmpty()) {
                    continue;
                }

                if (path.startsWith(rule) && (best == null || rule.length() > best.length())) {
                    best = rule;
                }
            }
            return best;
        }

        private static String stripComment(String line) {
            int commentIndex = line.indexOf('#');
            if (commentIndex < 0) {
                return line;
            }
            return line.substring(0, commentIndex);
        }

        private static String normalizeRule(String value) {
            if (value == null) {
                return null;
            }

            String cleaned = value.trim();
            if (cleaned.isBlank()) {
                return null;
            }
            if (!cleaned.startsWith("/")) {
                cleaned = "/" + cleaned;
            }

            int wildcardIndex = cleaned.indexOf('*');
            if (wildcardIndex >= 0) {
                cleaned = cleaned.substring(0, wildcardIndex);
            }

            return cleaned;
        }
    }

    private CrawlStatus snapshot(String currentUrl) {
        return new CrawlStatus(
                discoveredCount.get(),
                visited.size(),
                queue.size(),
                activeWorkers.get(),
                currentUrl
        );
    }

    private void clearState() {
        queue.clear();
        visited.clear();
        scheduled.clear();
        robotsRulesCache.clear();
        discoveredCount.set(0);
        activeWorkers.set(0);
        pendingUrls.set(0);
        lastUrl.set(null);
    }

    private void clearExecutionRefs() {
        running.set(false);
        executor = null;
    }

    private void sleepQuietly() {
        try {
            Thread.sleep(IDLE_BACKOFF_MS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            running.set(false);
        }
    }
}
