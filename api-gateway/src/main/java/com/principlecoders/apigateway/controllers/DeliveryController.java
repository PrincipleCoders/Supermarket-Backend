package com.principlecoders.apigateway.controllers;

import com.principlecoders.common.helpers.WebClientErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.principlecoders.common.utils.ServiceApiKeys.DELIVERY_API_KEY;
import static com.principlecoders.common.utils.ServiceUrls.DELIVERY_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping("delivery/")
public class DeliveryController {
    private final WebClient webClient;
    private final WebClientErrorHandler webClientErrorHandler;

    @GetMapping("order/ready/all")
    public Mono<?> getAllReadyOrders() {
        String delUrl = DELIVERY_URL + "order/ready/all";

        return webClient.get()
                .uri(delUrl)
                .header("api-key", DELIVERY_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }

    @PutMapping("markToDeliverStatus/{orderId}/{status}")
    public Mono<?> updateMarkToDeliverStatus(@PathVariable String orderId, @PathVariable boolean status) {
        String delUrl = DELIVERY_URL + "markToDeliverStatus/" + orderId + "/" + status;

        return webClient.put()
                .uri(delUrl)
                .header("api-key", DELIVERY_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }


    @PutMapping("delivered/{orderId}/{status}")
    public Mono<?> updateDelivered(@PathVariable String orderId, @PathVariable boolean status) {
        String delUrl = DELIVERY_URL + "delivered/" + orderId + "/" + status;

        return webClient.put()
                .uri(delUrl)
                .header("api-key", DELIVERY_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }

}
