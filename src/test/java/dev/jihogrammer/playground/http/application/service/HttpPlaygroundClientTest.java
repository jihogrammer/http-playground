package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.domain.HttpMethod;
import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

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
        var hgRequest = new HttpPlaygroundRequest(
                HttpMethod.GET,
                "https://www.google.com/search",
                null,
                Map.of("q", List.of("hello+world")),
                null
        );

        // then
        assertThatCode(() -> this.client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void postGoogle() {
        // given
        var hgRequest = new HttpPlaygroundRequest(
                HttpMethod.POST,
                "https://www.google.com/search",
                null,
                null,
                ""
        );

        // then
        assertThatCode(() -> this.client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void putGoogle() {
        // given
        var hgRequest = new HttpPlaygroundRequest(
                HttpMethod.PUT,
                "https://www.google.com/search",
                null,
                null,
                ""
        );

        // then
        assertThatCode(() -> this.client.send(hgRequest)).doesNotThrowAnyException();
    }

    @Test
    void deleteGoogle() {
        // given
        var hgRequest = new HttpPlaygroundRequest(
                HttpMethod.DELETE,
                "https://www.google.com/search",
                null,
                null,
                null
        );

        // then
        assertThatCode(() -> this.client.send(hgRequest)).doesNotThrowAnyException();
    }

}