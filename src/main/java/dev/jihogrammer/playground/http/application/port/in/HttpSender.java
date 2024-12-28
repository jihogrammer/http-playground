package dev.jihogrammer.playground.http.application.port.in;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import dev.jihogrammer.playground.http.domain.HttpPlaygroundResponse;

import java.util.concurrent.CompletableFuture;

public interface HttpSender {

    HttpPlaygroundResponse send(HttpPlaygroundRequest hgRequest);

    CompletableFuture<HttpPlaygroundResponse> sendAsync(HttpPlaygroundRequest hgRequest);

}
