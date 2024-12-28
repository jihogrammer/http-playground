package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatCode;

class HttpPlaygroundClientTest {

    static HttpPlaygroundClient client;

    @BeforeAll
    static void setUpClass() {
        var config = new HttpPlaygroundServiceConfig();
        var executor = config.executor(5, 10, 50, "test-hp-");
        var httpClient = config.httpClient(30000, executor);
        var bodyHandler = config.bodyHandler();

        client = config.httpPlaygroundClient(httpClient, bodyHandler);
    }

    @Test
    void getGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.get("https://www.google.com/search")
                .queryParam("q", "hello+world")
                .build();

        // then
        assertThatCode(() -> client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void postGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.post("https://www.google.com/search")
                .body("{\"hello\": \"world\"}")
                .build();

        // then
        assertThatCode(() -> client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void putGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.put("https://www.google.com/search")
                .header("X-Request-ID", UUID.randomUUID() + "@TEST")
                .build();

        // then
        assertThatCode(() -> client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void deleteGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.delete("https://www.google.com/search").build();

        // then
        assertThatCode(() -> client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void sendAsync() {
        // given
        var a = HttpPlaygroundRequest.get("https://www.google.com").build();
        var b = HttpPlaygroundRequest.post("https://www.naver.com").build();
        var c = HttpPlaygroundRequest.put("https://www.chatgpt.com").build();
        var d = HttpPlaygroundRequest.delete("https://www.github.com").build();

        // when
        var futures = CompletableFuture.allOf(
                client.sendAsync(a),
                client.sendAsync(b),
                client.sendAsync(c),
                client.sendAsync(d)
        );

        // then
        assertThatCode(futures::join).doesNotThrowAnyException();
    }

}