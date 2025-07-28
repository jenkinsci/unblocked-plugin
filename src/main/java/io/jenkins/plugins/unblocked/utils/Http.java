package io.jenkins.plugins.unblocked.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import javax.annotation.Nullable;

public class Http {

    private static final String BASEURL = "https://dev.getunblocked.com";

    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    private static final HttpClient CLIENT =
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    public static HttpResponse<String> post(String body) {
        return post(BASEURL, body);
    }

    public static HttpResponse<String> post(@Nullable String baseUrl, String body) {
        final var url = String.format("%s/api/hooks/jenkins", baseUrl != null ? baseUrl : BASEURL);
        final var payload = HttpRequest.BodyPublishers.ofString(body);
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(payload)
                .timeout(TIMEOUT)
                .build();

        try {
            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
