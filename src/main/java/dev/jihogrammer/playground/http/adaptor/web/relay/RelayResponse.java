package dev.jihogrammer.playground.http.adaptor.web.relay;

import dev.jihogrammer.playground.http.domain.HttpPlaygroundResponse;

import java.util.List;
import java.util.Map;

record RelayResponse(
        int statusCode,
        Map<String, List<String>> headers,
        String body
) {

    public static RelayResponse of(final HttpPlaygroundResponse response) {
        return new RelayResponse(
                response.statusCode(),
                response.headers(),
                response.body());
    }

}
