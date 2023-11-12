package com.principlecoders.apigateway.controllers;

import com.principlecoders.common.helpers.WebClientErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.principlecoders.common.utils.ServiceApiKeys.INVENTORY_API_KEY;
import static com.principlecoders.common.utils.ServiceUrls.INVENTORY_URL;

@RestController
@RequiredArgsConstructor
public class InventoryController {
    private final WebClient webClient;
    private final WebClientErrorHandler webClientErrorHandler;

    @DeleteMapping("cart/{cartId}/product/{productId}")
    public Mono<?> deleteProduct(@PathVariable String cartId,@PathVariable String productId) {
        String invUrl = INVENTORY_URL + "cart/" + cartId + "/product/" + productId;
        return webClient.delete()
                .uri(invUrl)
                .header("api-key", INVENTORY_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }
}
