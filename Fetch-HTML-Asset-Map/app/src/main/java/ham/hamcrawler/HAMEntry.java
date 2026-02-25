package ham.hamcrawler;

import ham.hamcrawler.engine.CrawlerEngine;
import ham.hamcrawler.model.CrawlCallbacks;
import ham.hamcrawler.model.CrawlOptions;

public class HAMEntry {
	private final CrawlerEngine crawlerEngine;

	public HAMEntry() {
		this.crawlerEngine = new CrawlerEngine();
	}

	public void startCrawl(CrawlOptions options, CrawlCallbacks callbacks) {
		crawlerEngine.start(options, callbacks);
	}

	public void stopCrawl() {
		crawlerEngine.stop();
	}

	public boolean isRunning() {
		return crawlerEngine.isRunning();
    }
}
