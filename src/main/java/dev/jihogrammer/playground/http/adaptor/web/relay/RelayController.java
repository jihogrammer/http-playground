package dev.jihogrammer.playground.http.adaptor.web.relay;

import dev.jihogrammer.playground.http.application.port.in.HttpSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
class RelayController {

    private final HttpSender httpSender;

    @PostMapping("/relay")
    @ResponseBody
    RelayResponse relay(@RequestBody final RelayPayload payload) {
        return RelayResponse.of(this.httpSender.send(payload.toHttpPlaygroundRequest()));
    }

    @GetMapping("/")
    String relayView() {
        return "relay";
    }

}
