package dev.jihogrammer.playground.http.application.service;

import dev.jihogrammer.playground.http.domain.HttpMethod;
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
        var url = "https://www.google.com/search";
        var queryParams = Map.of("q", List.of("hello+world"));

        // then
        assertThatCode(() -> this.client.send(HttpMethod.GET, url, null, queryParams, null))
                .doesNotThrowAnyException();
    }

    @Test
    void postGoogle() {
        // given
        var url = "https://www.google.com/search";

        // then
        assertThatCode(() -> this.client.send(HttpMethod.POST, url, null, null, ""))
                .doesNotThrowAnyException();
    }

    @Test
    void putGoogle() {
        // given
        var url = "https://www.google.com/search";

        // then
        assertThatCode(() -> this.client.send(HttpMethod.PUT, url, null, null, ""))
                .doesNotThrowAnyException();
    }

    @Test
    void deleteGoogle() {
        // given
        var url = "https://www.google.com/search";

        // then
        assertThatCode(() -> this.client.send(HttpMethod.DELETE, url, null, null, null))
                .doesNotThrowAnyException();
    }

}