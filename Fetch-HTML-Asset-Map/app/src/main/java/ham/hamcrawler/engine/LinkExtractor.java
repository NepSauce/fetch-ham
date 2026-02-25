package ham.hamcrawler.engine;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LinkExtractor {
    public Set<String> extractLinks(Document document, String pageUrl) {
        Set<String> results = new LinkedHashSet<>();
        URI pageUri = toUri(pageUrl);
        if (pageUri == null) {
            return results;
        }

        for (Element element : document.select("a[href], link[href], script[src], img[src], source[src], iframe[src], audio[src], video[src]")) {
            String candidate = element.hasAttr("href") ? element.absUrl("href") : element.absUrl("src");
            if (candidate == null || candidate.isBlank()) {
                continue;
            }

            URI uri = toUri(candidate);
            if (uri == null || uri.getScheme() == null) {
                continue;
            }

            String normalized = normalize(uri);
            if (normalized != null) {
                results.add(normalized);
            }
        }

        return results;
    }

    public URI toUri(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            URI uri = URI.create(value.trim());
            if (uri.getHost() == null || uri.getScheme() == null) {
                return null;
            }

            String scheme = uri.getScheme().toLowerCase();
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                return null;
            }

            return uri;
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public String normalize(URI uri) {
        try {
            int port = uri.getPort();
            String scheme = uri.getScheme().toLowerCase();
            if (("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443)) {
                port = -1;
            }

            String path = uri.getPath();
            if (path == null || path.isBlank()) {
                path = "/";
            } else if (path.length() > 1 && path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            URI clean = new URI(
                    scheme,
                    uri.getUserInfo(),
                    uri.getHost().toLowerCase(),
                    port,
                    path,
                    uri.getQuery(),
                    null
            );
            return clean.toString();
        } catch (Exception exception) {
            return null;
        }
    }
}
