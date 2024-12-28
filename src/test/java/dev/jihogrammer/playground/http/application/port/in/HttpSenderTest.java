package dev.jihogrammer.playground.http.application.port.in;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HttpSenderTest {

    @Test
    void sendAsync(@Autowired final HttpSender client) {
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