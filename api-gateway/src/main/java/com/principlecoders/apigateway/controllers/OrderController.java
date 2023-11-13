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

    @PutMapping("cart")
    public Mono<?> addToCart(@RequestBody Object cart) {
        String ordUrl = ORDER_URL + "cart";

        return webClient.put()
                .uri(ordUrl)
                .header("api-key", ORDER_API_KEY)
                .bodyValue(cart)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }


    @GetMapping("cart/user/{userId}")
    public Mono<?> getCartOfUser(@PathVariable String userId) {
        String ordUrl = ORDER_URL + "cart/user/" + userId;

        return webClient.get()
                .uri(ordUrl)
                .header("api-key", ORDER_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }

    @DeleteMapping("cart/user/{userId}/product/{productId}")
    public Mono<?> deleteCartItem(@PathVariable String userId, @PathVariable String productId) {
        String ordUrl = ORDER_URL + "cart/user/" + userId + "/product/" + productId;

        return webClient.delete()
                .uri(ordUrl)
                .header("api-key", ORDER_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }


    @PostMapping("checkout/user/{userId}")
    public Mono<?> checkout(@PathVariable String userId) {
        String ordUrl = ORDER_URL + "checkout/user/" + userId;

        return webClient.post()
                .uri(ordUrl)
                .header("api-key", ORDER_API_KEY)
                .bodyValue(userId)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }
}