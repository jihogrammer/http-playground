package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThatCode;

class HttpPlaygroundClientTest {

    static HttpPlaygroundClient client;

    @BeforeAll
    static void setUpClass() {
        System.setProperty("jdk.httpclient.keepAlive.timeout", "99999");
        var httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.of(30, ChronoUnit.SECONDS))
                .executor(Executors.newFixedThreadPool(5))
                .build();
        var bodyHandler = HttpResponse.BodyHandlers.ofString(Charset.defaultCharset());

        client = new HttpPlaygroundClient(httpClient, bodyHandler);
    }

    @AfterAll
    static void tearDownClass() {
        client.close();
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