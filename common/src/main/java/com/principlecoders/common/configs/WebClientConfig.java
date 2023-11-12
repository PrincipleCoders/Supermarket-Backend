package com.principlecoders.common.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient initWebClient() {
        return WebClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .filter((request, next) -> {
                    System.out.println("Request: " + request.method() + " " + request.url() + " " + request.headers());
                    return next.exchange(request);
                })
                .build();
    }
}
