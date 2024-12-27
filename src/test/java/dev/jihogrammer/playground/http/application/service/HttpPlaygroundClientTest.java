package dev.jihogrammer.playground.http.application.service;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;

class HttpPlaygroundClientTest {

    HttpPlaygroundClient client;

    @BeforeEach
    void setUp() {
        this.client = new HttpPlaygroundClient(Charset.defaultCharset());
    }

    @Test
    void google() {
        // given
        var url =  "https://www.google.com/search";
        var queryParams = Map.of("q", List.of("hello+world"));

        // when
        ThrowingCallable when = () -> this.client.send(url, null, queryParams);

        // then
        assertThatCode(when).doesNotThrowAnyException();
    }

}