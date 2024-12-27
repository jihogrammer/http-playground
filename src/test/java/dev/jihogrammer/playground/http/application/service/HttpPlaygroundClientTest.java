package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;

class HttpPlaygroundClientTest {

    HttpPlaygroundClient client;

    @BeforeEach
    void setUp() {
        var httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.of(30, ChronoUnit.SECONDS))
                .build();
        var bodyHandler = HttpResponse.BodyHandlers.ofString(Charset.defaultCharset());

        this.client = new HttpPlaygroundClient(httpClient, bodyHandler);
    }

    @AfterEach
    void tearDown() {
        this.client.close();
    }

    @Test
    void getGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.get("https://www.google.com/search")
                .queryParam("q", "hello+world")
                .build();

        // then
        assertThatCode(() -> this.client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void postGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.post("https://www.google.com/search")
                .body("{\"hello\": \"world\"}")
                .build();

        // then
        assertThatCode(() -> this.client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void putGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.put("https://www.google.com/search")
                .header("X-Request-ID", UUID.randomUUID() + "@TEST")
                .build();

        // then
        assertThatCode(() -> this.client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void deleteGoogle() {
        // given
        var hgRequest = HttpPlaygroundRequest.delete("https://www.google.com/search").build();

        // then
        assertThatCode(() -> this.client.send(hgRequest)).doesNotThrowAnyException();
    }

}