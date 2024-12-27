package dev.jihogrammer.playground.http.application.port.in;

import java.util.List;
import java.util.Map;

public interface HttpSender {

    void send(String url, Map<String, List<String>> headers, Map<String, List<String>> queryParams);

}
