package com.principlecoders.apigateway.controllers;

import com.principlecoders.common.helpers.WebClientErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.principlecoders.common.utils.ServiceApiKeys.ORDER_API_KEY;
import static com.principlecoders.common.utils.ServiceUrls.ORDER_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping("order/")
public class OrderController {
    private final WebClient webClient;
    private final WebClientErrorHandler webClientErrorHandler;


    @GetMapping("user/{userId}")
    public Mono<?> getAllOrdersOfUser(@PathVariable String userId) {
        String ordUrl = ORDER_URL + "user/" + userId;

        return webClient.get()
                .uri(ordUrl)
                .header("api-key", ORDER_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }
}