package dev.jihogrammer.playground.http.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Configuration
class HttpPlaygroundServiceConfig {

    @Bean
    ThreadPoolTaskExecutor executor(
            @Value("${playground.http.executor.pool.core-size:5}") final int coreSize,
            @Value("${playground.http.executor.pool.max-size:10}") final int maxSize,
            @Value("${playground.http.executor.pool.queue-capacity:50}") final int queueCapacity,
            @Value("${playground.http.executor.pool.thread-name-prefix:http-playground-}") final String threadNamePrefix
    ) {
        log.info("ThreadPoolTaskExecutor - coreSize: {}", coreSize);
        log.info("ThreadPoolTaskExecutor - maxSize: {}", maxSize);
        log.info("ThreadPoolTaskExecutor - queueCapacity: {}", queueCapacity);
        log.info("ThreadPoolTaskExecutor - threadNamePrefix: {}", threadNamePrefix);

        var executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();

        return executor;
    }

    @Bean
    HttpClient httpClient(
            @Value("${playground.http.client.time-out-millis:10000}") final long timeOutMillis,
            final ThreadPoolTaskExecutor executor
    ) {
        log.info("HttpClient - timeOutMillis: {}", timeOutMillis);

        return HttpClient.newBuilder()
                .connectTimeout(Duration.of(timeOutMillis, ChronoUnit.MILLIS))
                .executor(executor)
                .build();
    }

    @Bean
    HttpResponse.BodyHandler<String> bodyHandler() {
        return HttpResponse.BodyHandlers.ofString(Charset.defaultCharset());
    }

    @Bean
    HttpPlaygroundClient httpPlaygroundClient(final HttpClient httpClient, final HttpResponse.BodyHandler<String> bodyHandler) {
        return new HttpPlaygroundClient(httpClient, bodyHandler);
    }

}
