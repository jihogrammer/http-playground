package dev.jihogrammer.playground.http.application.port.in;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class HttpSenderTest {

    @Test
    void sendAsync(@Autowired final HttpSender<Void> client) {
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