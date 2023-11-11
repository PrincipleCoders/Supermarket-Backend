package com.principlecoders.common.helpers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class WebClientErrorHandler {
    public Mono<? extends ResponseEntity<Object>> handle(Throwable error) {
        if (!(error instanceof WebClientResponseException)) {
            return Mono.error(error);
        }
        WebClientResponseException exception = (WebClientResponseException) error;
        return Mono.just(
                ResponseEntity.status(exception.getStatusCode())
                        .headers(exception.getHeaders())
                        .body(exception.getResponseBodyAsByteArray())
        );
    }
}
