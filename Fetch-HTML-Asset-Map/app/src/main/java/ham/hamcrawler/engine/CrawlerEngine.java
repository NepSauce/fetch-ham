package ham.hamcrawler.engine;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private static final long IDLE_BACKOFF_MS = 50;

    private final FetchService fetchService;
    private final LinkExtractor linkExtractor;

    private final ConcurrentLinkedQueue<String> queue;
    private final Set<String> visited;

    private ExecutorService executor;
    private AtomicBoolean running;
    private AtomicBoolean stopRequested;
    private AtomicInteger activeWorkers;
    private AtomicInteger discoveredCount;
    private AtomicReference<String> lastUrl;

    public CrawlerEngine() {
        this.fetchService = new FetchService();
        this.linkExtractor = new LinkExtractor();
        this.queue = new ConcurrentLinkedQueue<>();
        this.visited = ConcurrentHashMap.newKeySet();
        this.running = new AtomicBoolean(false);
        this.stopRequested = new AtomicBoolean(false);
        this.activeWorkers = new AtomicInteger(0);
        this.discoveredCount = new AtomicInteger(0);
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
        discoveredCount.incrementAndGet();
        HamBugLogger.log("Crawl started | url=" + options.startUrl() + " | mode=" + options.mode() + " | threads=" + threadCount);

        callbacks.onStarted(options);

        executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> workerLoop(options, callbacks));
        }

        Thread completionWatcher = new Thread(() -> {
            try {
                executor.shutdown();
                boolean terminated = executor.awaitTermination(7, TimeUnit.DAYS);
                if (!terminated) {
                    HamBugLogger.log("Crawl termination timeout while waiting for workers.");
                    callbacks.onError("Crawler termination timeout.", null);
                }
                finalizeRun(callbacks);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                HamBugLogger.log("Crawl interrupted while waiting for completion.");
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
                if (activeWorkers.get() == 0 && queue.isEmpty()) {
                    running.set(false);
                    break;
                }

                sleepQuietly();
                continue;
            }

            if (!visited.add(nextUrl)) {
                continue;
            }

            activeWorkers.incrementAndGet();
            lastUrl.set(nextUrl);
            callbacks.onProgress(snapshot(nextUrl));
            try {
                Document document = fetchService.fetch(nextUrl);
                if (document == null) {
                    HamBugLogger.log("Thread-" + Thread.currentThread().threadId() + " visited=" + nextUrl + " | title=<non-html>");
                    continue;
                }

                String pageTitle = document.title();
                if (pageTitle == null || pageTitle.isBlank()) {
                    pageTitle = "(untitled page)";
                }
                HamBugLogger.log("Thread-" + Thread.currentThread().threadId() + " visited=" + nextUrl + " | title=" + pageTitle);

                Set<String> discoveredUrls = linkExtractor.extractLinks(document, nextUrl);
                HamBugLogger.log("Thread-" + Thread.currentThread().threadId() + " discovered=" + discoveredUrls.size() + " links/assets | page=" + nextUrl);

                for (String discoveredUrl : discoveredUrls) {
                    if (!isAllowedByMode(startUri, discoveredUrl, options.mode())) {
                        continue;
                    }

                    if (!visited.contains(discoveredUrl)) {
                        queue.offer(discoveredUrl);
                        discoveredCount.incrementAndGet();
                        HamBugLogger.log("Thread-" + Thread.currentThread().threadId() + " found=" + discoveredUrl + " | from=" + nextUrl);
                    }
                }
            } catch (Exception exception) {
                HamBugLogger.log("Thread-" + Thread.currentThread().threadId() + " error=" + nextUrl + " | message=" + exception.getMessage());
                callbacks.onError("Error crawling: " + nextUrl, exception);
            } finally {
                activeWorkers.decrementAndGet();
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

    private void finalizeRun(CrawlCallbacks callbacks) {
        CrawlStatus finalStatus = snapshot(lastUrl.get());
        boolean stoppedByUser = stopRequested.get();

        clearExecutionRefs();

        if (stoppedByUser) {
            HamBugLogger.log("Crawl stopped | visited=" + finalStatus.visitedCount() + " | discovered=" + finalStatus.discoveredCount() + " | queued=" + finalStatus.queuedCount());
            callbacks.onStopped(finalStatus);
        } else {
            HamBugLogger.log("Crawl completed | visited=" + finalStatus.visitedCount() + " | discovered=" + finalStatus.discoveredCount() + " | queued=" + finalStatus.queuedCount());
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
        discoveredCount.set(0);
        activeWorkers.set(0);
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
