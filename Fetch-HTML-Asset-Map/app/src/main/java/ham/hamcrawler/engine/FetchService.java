package ham.hamcrawler.engine;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetchService {
    private static final String USER_AGENT = "FetchHam/1.0";
    private static final int TIMEOUT_MS = 10_000;

    public Document fetch(String url) throws IOException {
        Connection connection = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT_MS)
                .followRedirects(true)
                .ignoreHttpErrors(true);

        Connection.Response response = connection.execute();
        String contentType = response.contentType();
        if (contentType != null && !contentType.toLowerCase().contains("text/html")) {
            return null;
        }

        return response.parse();
    }
}
