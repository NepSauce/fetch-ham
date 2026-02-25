package ham.hamcrawler.model;

public record CrawlOptions(
        String startUrl,
        CrawlMode mode,
        boolean respectRobots,
        boolean enableRules,
        boolean extractNonHtml,
        boolean extractMetadata,
        int threadCount
) {
}
