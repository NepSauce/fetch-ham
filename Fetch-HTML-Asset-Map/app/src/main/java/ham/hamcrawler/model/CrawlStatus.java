package ham.hamcrawler.model;

public record CrawlStatus(
        int discoveredCount,
        int visitedCount,
        int queuedCount,
        int activeWorkers,
        String currentUrl
) {
}
