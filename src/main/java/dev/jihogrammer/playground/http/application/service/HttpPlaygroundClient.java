package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.application.port.in.HttpSender;
import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
class HttpPlaygroundClient implements HttpSender<Void>, AutoCloseable {

    private final HttpClient httpClient;

    private final HttpResponse.BodyHandler<String> bodyHandler;

    @Override
    public void send(final HttpPlaygroundRequest hgRequest) {
        try {
            this.sendAsync(hgRequest).join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Void> sendAsync(final HttpPlaygroundRequest hgRequest) {
        try {
            return this.httpClient.sendAsync(hgRequest.getHttpRequest(), bodyHandler)
                    .thenAccept(response -> this.log(hgRequest, response));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void log(final HttpPlaygroundRequest hgRequest, final HttpResponse<String> response) {
        log.info("\n>>> REQUEST\n{} {}\n{}",
                hgRequest.method(),
                hgRequest.uri(),
                this.parseHeaders(hgRequest.headers()));
        log.info("\n>>> RESPONSE\n{}\n{}",
                this.parseHeaders(response.headers()),
                response.body());
    }

    private StringBuilder parseHeaders(final HttpHeaders headers) {
        var sb = new StringBuilder();

        for (var k : headers.map().keySet()) {
            for (var v : headers.map().get(k)) {
                sb.append(k).append(": ").append(v).append('\n');
            }
        }

        return sb;
    }

    @Override
    public void close() {
        this.httpClient.close();
    }

}
