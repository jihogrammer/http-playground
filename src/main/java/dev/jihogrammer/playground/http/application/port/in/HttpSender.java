package dev.jihogrammer.playground.http.application.port.in;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;

public interface HttpSender {

    void send(HttpPlaygroundRequest hgRequest);

}
