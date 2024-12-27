package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.application.port.in.HttpSender;
import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@RequiredArgsConstructor
@Slf4j
class HttpPlaygroundClient implements HttpSender, AutoCloseable {

    private final HttpClient httpClient;

    private final HttpResponse.BodyHandler<String> bodyHandler;

    @Override
    public void send(final HttpPlaygroundRequest hgRequest) {
        try {
            var response = this.httpClient.send(hgRequest.toHttpRequest(), bodyHandler);

            log.info("statusCode: {}", response.statusCode());
            log.info("request uri: {}", hgRequest.uri());
            log.info("request headers: {}", hgRequest.headers());
            log.info("response headers: {}", response.headers());
            log.info("response body: {}", response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        this.httpClient.close();
    }

}
