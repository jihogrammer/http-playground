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

        return String.valueOf(this.response.version())
                + ' '
                + this.response.statusCode()
                + '\n'
                + this.response.headers()
                + (body == null ? "" : "\n\n" + body);
    }
}
