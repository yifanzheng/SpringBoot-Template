package top.yifan.util;

import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

/**
 * URLUtil
 *
 * @author sz7v
 */
public class URLUtil {

    private URLUtil() {
    }

    public static String fullURL(@NotNull String endpoint, @NotNull String uri) {
        Assert.notNull(endpoint, "Endpoint can't be null");
        Assert.notNull(uri, "URI can't be null");

        if (endpoint.endsWith("/")) {
            if (uri.startsWith("/")) {
                uri = uri.substring(1);
                return endpoint + uri;
            }
            return endpoint + uri;
        }
        if (uri.startsWith("/")) {
            return endpoint + uri;
        }

        return endpoint + "/" + uri;
    }

}