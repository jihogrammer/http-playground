package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

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

        // when
        var response = client.send(hgRequest);

        // then
        assertThat(response.isOk()).isTrue();
    }

    @Test
    void postGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.post("https://www.google.com/search")
                .body("{\"hello\": \"world\"}")
                .build();

        // when
        var response = client.send(hgRequest);

        // then
        assertThat(response.isBadRequest()).isTrue();
    }

    @Test
    void putGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.put("https://www.google.com/search")
                .header("X-Request-ID", UUID.randomUUID() + "@TEST")
                .build();

        // when
        var response = client.send(hgRequest);

        // then
        assertThat(response.isBadRequest()).isTrue();
    }

    @Test
    void deleteGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.delete("https://www.google.com/search").build();

        // when
        var response = client.send(hgRequest);

        // then
        assertThat(response.isBadRequest()).isTrue();
    }

    @Test
    void sendAsync() {
        // given
        var a = client.sendAsync(HttpPlaygroundRequest.get("https://www.google.com").build());
        var b = client.sendAsync(HttpPlaygroundRequest.post("https://www.naver.com").build());
        var c = client.sendAsync(HttpPlaygroundRequest.put("https://www.chatgpt.com").build());
        var d = client.sendAsync(HttpPlaygroundRequest.delete("https://www.github.com").build());

        // when
        CompletableFuture.allOf(a, b, c, d).thenRun(() -> {
            try {
                var aRes = a.get();
                var bRes = b.get();
                var cRes = c.get();
                var dRes = d.get();

                // then
                assertThat(aRes.isOk()).isTrue();
                assertThat(bRes.isOk()).isTrue();
                assertThat(cRes.isRedirection()).isTrue();
                assertThat(dRes.isRedirection()).isTrue();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).join();
    }

}