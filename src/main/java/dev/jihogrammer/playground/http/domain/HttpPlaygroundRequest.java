package dev.jihogrammer.playground.http.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpPlaygroundRequest {

    private static final String DEFAULT_HEADER_NAME = "x-request-id";

    private final HttpRequest request;

    private final String body;

    private HttpPlaygroundRequest(final HttpRequest request, final String body) {
        if (request == null) {
            throw new IllegalArgumentException("request is null.");
        }
        this.request = request;
        this.body = body;
    }

    public static Builder get(final String url) {
        return new Builder(HttpMethod.GET, url);
    }

    public static Builder post(final String url) {
        return new Builder(HttpMethod.POST, url);
    }

    public static Builder put(final String url) {
        return new Builder(HttpMethod.PUT, url);
    }

    public static Builder delete(final String url) {
        return new Builder(HttpMethod.DELETE, url);
    }

    public HttpRequest getHttpRequest() {
        return this.request;
    }

    public String method() {
        return this.request.method();
    }

    public URI uri() {
        return this.request.uri();
    }

    public HttpHeaders headers() {
        return this.request.headers();
    }

    public String body() {
        return this.body;
    }

    @Override
    public String toString() {
        var headers = this.headers().map();
        var headerBuilder = new StringBuilder();

        for (var k : headers.keySet()) {
            for (var v : headers.get(k)) {
                headerBuilder.append(k).append(": ").append(v).append('\n');
            }
        }

        return (this.method() + ' ' + this.uri() + '\n')
                + headerBuilder
                + (this.body() == null ? "" : "\n\n" + this.body());
    }

    public static class Builder {

        private final HttpMethod method;

        private final String url;

        private final Set<Pair> headers;

        private final Set<Pair> queryParams;

        private String body;

        private Builder(final HttpMethod method, final String url) {
            if (method == null) {
                throw new IllegalArgumentException("method is null.");
            }
            if (url == null || url.isBlank()) {
                throw new IllegalArgumentException("url is blank.");
            }
            this.method = method;
            this.url = url;
            this.headers = new HashSet<>();
            this.queryParams = new HashSet<>();
        }

        public HttpPlaygroundRequest build() {
            if (this.method == null) {
                throw new IllegalArgumentException("method is null.");
            }
            if (this.url == null || this.url.isBlank()) {
                throw new IllegalArgumentException("url is blank.");
            }

            return new HttpPlaygroundRequest(this.request(), this.body);
        }

        public Builder header(final String key, final String value) {
            final var k = key.toLowerCase();
            this.headers.add(new Pair(k, value));
            return this;
        }

        public Builder queryParam(final String key, final String value) {
            this.queryParams.add(new Pair(key, value));
            return this;
        }

        public Builder body(final String body) {
            this.body = body;
            return this;
        }

        private HttpRequest request() {
            var requestBuilder = HttpRequest.newBuilder()
                    .uri(this.uri())
                    .headers(this.rawHeaders());

            var bodyPublisher = switch (this.method) {
                case POST, PUT -> HttpRequest.BodyPublishers.ofString(this.body == null ? "" : this.body);
                default -> null;
            };

            return switch (this.method) {
                case GET -> requestBuilder.GET().build();
                case POST -> requestBuilder.POST(bodyPublisher).build();
                case PUT -> requestBuilder.PUT(bodyPublisher).build();
                case DELETE -> requestBuilder.DELETE().build();
            };
        }

        private URI uri() {
            try {
                if (this.queryParams == null || this.queryParams.isEmpty()) {
                    return new URI(this.url);
                } else {
                    return new URI(String.join("?", this.url, this.queryString()));
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        private String[] rawHeaders() {
            if (this.headers.stream().noneMatch(p -> DEFAULT_HEADER_NAME.equalsIgnoreCase(p.k()))) {
                this.header(DEFAULT_HEADER_NAME, UUID.randomUUID() + "@HG");
            }

            return this.headers.stream()
                    .flatMap(pair -> Stream.of(pair.k(), pair.v()))
                    .toArray(String[]::new);
        }

        private String queryString() {
            return this.queryParams.stream()
                    .map(pair -> pair.k() + '=' + pair.v())
                    .collect(Collectors.joining("&"));
        }

    }

    private record Pair(String k, String v) {
    }

}
