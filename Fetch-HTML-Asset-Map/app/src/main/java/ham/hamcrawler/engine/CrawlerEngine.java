package ham.hamcrawler.engine;

import java.net.URI;
import java.util.Locale;
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

import org.jsoup.nodes.Document;

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

        int threadCount = Math.max(1, options.threadCount());
        queue.offer(options.startUrl());
        scheduled.add(options.startUrl());
        discoveredCount.incrementAndGet();
        pendingUrls.set(1);
        HamBugLogger.log("[HAM] Crawl started | url=" + options.startUrl() + " | mode=" + options.mode() + " | threads=" + threadCount);

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
        URI startUri = linkExtractor.toUri(options.startUrl());

        while (running.get()) {
            String nextUrl = queue.poll();
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
                Document document = fetchService.fetch(nextUrl);
                if (document == null) {
                    HamBugLogger.log("[" + workerName + "] visited=" + nextUrl + " | title=<non-html>");
                    continue;
                }

                String pageTitle = document.title();
                if (pageTitle == null || pageTitle.isBlank()) {
                    pageTitle = "(untitled page)";
                }
                HamBugLogger.log("[" + workerName + "] visited=" + nextUrl + " | title=" + pageTitle);

                Set<String> discoveredUrls = linkExtractor.extractLinks(document, nextUrl);
                HamBugLogger.log("[" + workerName + "] discovered=" + discoveredUrls.size() + " links/assets | page=" + nextUrl);

                for (String discoveredUrl : discoveredUrls) {
                    if (!isAllowedByMode(startUri, discoveredUrl, options.mode())) {
                        continue;
                    }

                    if (!shouldQueueDiscoveredUrl(discoveredUrl, options)) {
                        HamBugLogger.log("[" + workerName + "] skipped-non-html=" + discoveredUrl + " | from=" + nextUrl);
                        continue;
                    }

                    if (scheduled.add(discoveredUrl)) {
                        queue.offer(discoveredUrl);
                        pendingUrls.incrementAndGet();
                        discoveredCount.incrementAndGet();
                        HamBugLogger.log("[" + workerName + "] found=" + discoveredUrl + " | from=" + nextUrl);
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
