package dev.jihogrammer.playground.http.domain;

import java.net.http.HttpResponse;

public class HttpPlaygroundResponse {

    private final HttpResponse<String> response;

    public HttpPlaygroundResponse(final HttpResponse<String> response) {
        this.response = response;
    }

    public boolean isOk() {
        int statusCode = this.response.statusCode();
        return 200 <= statusCode && statusCode < 300;
    }

    public boolean isRedirection() {
        int statusCode = this.response.statusCode();
        return 300 <= statusCode && statusCode < 400;
    }

    public boolean isBadRequest() {
        int statusCode = this.response.statusCode();
        return 400 <= statusCode && statusCode < 500;
    }

    @Override
    public String toString() {
        var body = this.response.body();

        var headers = this.response.headers().map();
        var headerBuilder = new StringBuilder();

        for (var k : headers.keySet()) {
            if (":status".equalsIgnoreCase(k)) {
                continue;
            }
            for (var v : headers.get(k)) {
                headerBuilder.append(k).append(": ").append(v).append('\n');
            }
        }

        return (this.response.version() + " " + this.response.statusCode() + '\n')
                + headerBuilder
                + (body == null || body.isBlank() ? "" : "\n" + body);
    }

}
