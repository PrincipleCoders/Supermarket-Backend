package com.principlecoders.apigateway.controllers;

import com.principlecoders.common.dto.ProductDto;
import com.principlecoders.common.helpers.WebClientErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.principlecoders.common.utils.ServiceApiKeys.INVENTORY_API_KEY;
import static com.principlecoders.common.utils.ServiceUrls.INVENTORY_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping("inventory/")
public class InventoryController {
    private final WebClient webClient;
    private final WebClientErrorHandler webClientErrorHandler;


    @GetMapping("product/all")
    public Mono<?> getAllProducts() {
        String invUrl = INVENTORY_URL + "product/all";

        return webClient.get()
                .uri(invUrl)
                .header("api-key", INVENTORY_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }


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

    @PostMapping("product")
    public Mono<?> addProduct(@RequestBody ProductDto productDto) {
        String invUrl = INVENTORY_URL + "product";
        return webClient.post()
                .uri(invUrl)
                .header("api-key", INVENTORY_API_KEY)
                .body(Mono.just(productDto), ProductDto.class)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }

    @DeleteMapping("product/{productId}")
    public Mono<?> deleteProduct(@PathVariable String productId) {
        String ordUrl = INVENTORY_URL + "product/" + productId;

        return webClient.delete()
                .uri(ordUrl)
                .header("api-key", INVENTORY_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }
}
