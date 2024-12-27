package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.application.port.in.HttpSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@Slf4j
class HttpPlaygroundClient implements HttpSender {

    private final HttpClient httpClient;

    private final HttpResponse.BodyHandler<String> bodyHandler;

    @Override
    public void send(String url, Map<String, List<String>> headers, Map<String, List<String>> queryParams) {
        try {
            var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(this.convertToURI(url, queryParams))
                    .headers(this.convertToHeaderArray(headers))
                    .build();

            var response = this.httpClient.send(request, bodyHandler);

            log.info("statusCode: {}", response.statusCode());
            log.info("request uri: {}", request.uri());
            log.info("request headers: {}", request.headers());
            log.info("response headers: {}", response.headers());
            log.info("response body: {}", response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String[] convertToHeaderArray(final Map<String, List<String>> inputHeaders) {
        var headers = new HashMap<String, List<String>>();
        headers.put("X-Request-ID", List.of(UUID.randomUUID() + "@HG"));

        if (inputHeaders != null) {
            for (var key : inputHeaders.keySet()) {
                if ("X-Request-ID".equalsIgnoreCase(key)) {
                    headers.remove("X-Request-ID");
                    break;
                }
            }
        }

        return headers.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().flatMap(value -> Stream.of(entry.getKey().toLowerCase(), value)))
                .toArray(String[]::new);
    }

    private URI convertToURI(final String url, final Map<String, List<String>> queryParams) throws URISyntaxException {
        requireNonNull(url);

        if (queryParams == null || queryParams.isEmpty()) {
            return new URI(url);
        } else {
            var queryString = queryParams.entrySet().stream()
                    .flatMap(entry -> entry.getValue().stream().map(value -> entry.getKey() + '=' + value))
                    .collect(Collectors.joining("&"));

            return new URI(url + '?' + queryString);
        }
    }

    void close() {
        this.httpClient.close();
    }

}
