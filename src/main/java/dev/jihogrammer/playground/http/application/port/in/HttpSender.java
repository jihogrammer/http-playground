package dev.jihogrammer.playground.http.application.port.in;

import dev.jihogrammer.playground.http.domain.HttpMethod;

import java.util.List;
import java.util.Map;

public interface HttpSender {

    void send(HttpMethod httpMethod, String url, Map<String, List<String>> headers, Map<String, List<String>> queryParams, String body);

}
