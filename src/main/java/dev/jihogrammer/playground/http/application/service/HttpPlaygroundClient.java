package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.application.port.in.HttpSender;
import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import dev.jihogrammer.playground.http.domain.HttpPlaygroundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
class HttpPlaygroundClient implements HttpSender, AutoCloseable {

    private final HttpClient httpClient;

    private final HttpResponse.BodyHandler<String> bodyHandler;

    @Override
    public HttpPlaygroundResponse send(final HttpPlaygroundRequest hgRequest) {
        return this.sendAsync(hgRequest).join();
    }

    @Override
    public CompletableFuture<HttpPlaygroundResponse> sendAsync(final HttpPlaygroundRequest hgRequest) {
        try {
            return this.httpClient.sendAsync(hgRequest.getHttpRequest(), bodyHandler)
                    .thenApply(response -> {
                        var hpResponse = new HttpPlaygroundResponse(response);

                        log.info("\n>>> REQUEST\n{}\n\n>>> RESPONSE\n{}", hgRequest, hpResponse);

                        return hpResponse;
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
