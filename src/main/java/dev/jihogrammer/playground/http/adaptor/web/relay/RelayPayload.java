package dev.jihogrammer.playground.http.adaptor.web.relay;

import dev.jihogrammer.playground.http.domain.HttpMethod;
import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;

import java.util.List;
import java.util.Map;

record RelayPayload(
        HttpMethod method,
        String url,
        Map<String, List<String>> headers,
        Map<String, List<String>> queryParams,
        String body
) {

    RelayPayload {
        if (method == null) {
            throw new IllegalArgumentException("HttpMethod is null.");
        }
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL is null.");
        }
    }

    boolean hasHeaders() {
        return this.headers != null && !this.headers.isEmpty();
    }

    boolean hasQueryParams() {
        return this.queryParams != null && !this.queryParams.isEmpty();
    }

    HttpPlaygroundRequest toHttpPlaygroundRequest() {
        var builder = switch (this.method()) {
            case GET -> HttpPlaygroundRequest.get(this.url());
            case POST -> HttpPlaygroundRequest.post(this.url());
            case PUT -> HttpPlaygroundRequest.put(this.url());
            case DELETE -> HttpPlaygroundRequest.delete(this.url());
        };

        if (this.hasHeaders()) {
            var headers = this.headers();
            for (var k : headers.keySet()) {
                for (var v : headers.get(k)) {
                    builder.header(k, v);
                }
            }
        }

        if (this.hasQueryParams()) {
            var queryParams = this.queryParams();
            for (var k : queryParams.keySet()) {
                for (var v : queryParams.get(k)) {
                    builder.queryParam(k, v);
                }
            }
        }

        return builder.body(this.body()).build();
    }

}
