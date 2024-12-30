package dev.jihogrammer.playground.http.domain;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpPlaygroundResponse {

    private final HttpResponse<String> response;

    private final Map<String, List<String>> headers;

    public HttpPlaygroundResponse(final HttpResponse<String> response) {
        this.response = response;

        var tempHeaders = new HashMap<>(response.headers().map());
        tempHeaders.remove(":status");
        this.headers = Map.copyOf(tempHeaders);
    }

    public int statusCode() {
        return this.response.statusCode();
    }

    public Map<String, List<String>> headers() {
        return this.headers;
    }

    public String body() {
        return this.response.body();
    }

    public boolean isOk() {
        int statusCode = this.statusCode();
        return 200 <= statusCode && statusCode < 300;
    }

    public boolean isRedirection() {
        int statusCode = this.statusCode();
        return 300 <= statusCode && statusCode < 400;
    }

    public boolean isBadRequest() {
        int statusCode = this.statusCode();
        return 400 <= statusCode && statusCode < 500;
    }

    @Override
    public String toString() {
        var protocolBuilder = new StringBuilder()
                .append(this.response.version())
                .append(' ')
                .append(this.statusCode());

        var headerBuilder = new StringBuilder();
        for (var k : this.headers.keySet()) {
            for (var v : this.headers.get(k)) {
                headerBuilder.append(k).append(": ").append(v).append('\n');
            }
        }

        var body = this.response.body();
        var bodyBuilder = new StringBuilder();
        if (body != null && !body.isBlank()) {
            if (body.length() < 300) {
                bodyBuilder.append(body);
            } else {
                bodyBuilder.append(body.substring(0, 300)).append("...");
            }
        }

        return ""
                + protocolBuilder.append('\n')
                + headerBuilder.append('\n')
                + bodyBuilder.append('\n');
    }

}
