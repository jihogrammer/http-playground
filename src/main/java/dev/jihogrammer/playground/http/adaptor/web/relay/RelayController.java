package dev.jihogrammer.playground.http.adaptor.web.relay;

import dev.jihogrammer.playground.http.application.port.in.HttpSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relay")
@RequiredArgsConstructor
class RelayController {

    private final HttpSender httpSender;

    @PostMapping
    void relay(@RequestBody final RelayPayload payload) {
        this.httpSender.send(payload.toHttpPlaygroundRequest());
    }

}
