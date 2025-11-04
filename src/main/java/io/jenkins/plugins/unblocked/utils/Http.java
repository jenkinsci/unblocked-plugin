package io.jenkins.plugins.unblocked.utils;

import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.ProxyConfiguration;
import hudson.util.Secret;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Http {

    private static final Logger LOGGER = Logger.getLogger(Http.class.getName());

    private static final String BASEURL = "https://getunblocked.com";

    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    private static HttpClient createClient() {
        return ProxyConfiguration.newHttpClientBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    @Nullable
    public static HttpResponse<String> post(@Nullable String baseUrl, String eventType, String body, Secret signature) {
        if (signature == null) {
            LOGGER.log(Level.SEVERE, "Missing signature");
            return null;
        }

        final var url = String.format("%s/api/hooks/jenkins", baseUrl != null ? baseUrl : BASEURL);
        final var payload = HttpRequest.BodyPublishers.ofString(body);

        @SuppressWarnings("UastIncorrectHttpHeaderInspection")
        final var request = ProxyConfiguration.newHttpRequestBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .header("X-Jenkins-Event", eventType)
                .header("X-Jenkins-Signature", Hmac.sign(body, signature))
                .POST(payload)
                .timeout(TIMEOUT)
                .build();

        try {
            var client = createClient();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, e, () -> "request: " + url);
            return null;
        }
    }
}
