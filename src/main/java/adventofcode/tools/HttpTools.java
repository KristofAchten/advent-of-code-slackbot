package adventofcode.tools;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: kachte4
 * Date: 14-11-2020.
 */
public class HttpTools {

    public static String getRawContentFromPageWithSession(final URL leaderboardUrl,
                                                          final String sessionId) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) leaderboardUrl.openConnection();

            connection.setRequestProperty("Cookie", "session=" + sessionId);

            return IOUtils.toString(connection.getInputStream(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Error while fetching data from " + leaderboardUrl.toString(), e);
        }

    }

    public static void postMessageToHook(final String message,
                                         final String webhook) {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("text", message);

            final CloseableHttpClient client = HttpClients.createDefault();
            final HttpPost httpPost = new HttpPost(webhook);
            final StringEntity entity = new StringEntity(JsonTools.toJSON(m));

            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            client.execute(httpPost);
            client.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while constructing HTTP-request for " + webhook, e);
        }
    }
}
