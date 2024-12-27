package dev.jihogrammer.playground.http.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public record HttpPlaygroundRequest(
        HttpMethod method,
        String url,
        Map<String, List<String>> headers,
        Map<String, List<String>> queryParams,
        String body
) {

    public HttpPlaygroundRequest {
        requireNonNull(method);
        requireNonNull(url);
        if (url.isBlank()) {
            throw new IllegalArgumentException("url is blank.");
        }
        if (headers == null || headers.isEmpty()) {
            headers = new HashMap<>(Map.of("X-Request-ID", List.of(UUID.randomUUID() + "@HG")));
        }
    }

    public HttpRequest toHttpRequest() throws URISyntaxException {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(this.uri())
                .headers(this.toRawHeaders());

        return switch (method) {
            case GET -> requestBuilder.GET().build();
            case POST -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body)).build();
            case PUT -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body)).build();
            case DELETE -> requestBuilder.DELETE().build();
        };
    }

    public URI uri() throws URISyntaxException {
        if (this.queryParams == null || this.queryParams.isEmpty()) {
            return new URI(this.url);
        } else {
            var queryString = this.queryParams.entrySet().stream()
                    .flatMap(entry -> entry.getValue().stream().map(value -> entry.getKey() + '=' + value))
                    .collect(Collectors.joining("&"));

            return new URI(String.join("?", this.url, queryString));
        }
    }

    private String[] toRawHeaders() {
        return this.headers.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().flatMap(value -> Stream.of(entry.getKey().toLowerCase(), value)))
                .toArray(String[]::new);
    }

}
