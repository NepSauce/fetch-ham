package ham.hamcrawler.model;

public interface CrawlCallbacks {
    void onStarted(CrawlOptions options);

    void onProgress(CrawlStatus status);

    void onCompleted(CrawlStatus finalStatus);

    void onStopped(CrawlStatus finalStatus);

    void onError(String message, Throwable throwable);
}
