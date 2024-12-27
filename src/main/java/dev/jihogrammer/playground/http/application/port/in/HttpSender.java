package dev.jihogrammer.playground.http.application.port.in;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;

import java.util.concurrent.CompletableFuture;

public interface HttpSender<T> {

    void send(HttpPlaygroundRequest hgRequest);

    CompletableFuture<T> sendAsync(HttpPlaygroundRequest hgRequest);

}
