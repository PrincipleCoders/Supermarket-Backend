package com.principlecoders.orderservice.services;

import com.principlecoders.common.dto.CartItemDto;
import com.principlecoders.common.dto.CartProductsDto;
import com.principlecoders.common.dto.ProductDto;
import com.principlecoders.common.dto.RemainingOrderDto;
import com.principlecoders.orderservice.models.Cart;
import com.principlecoders.orderservice.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.principlecoders.common.utils.ServiceUrls.INVENTORY_URL;
import static com.principlecoders.common.utils.ServiceUrls.USER_URL;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartRepository cartRepository;
    private final WebClient webClient;

    public ResponseEntity<?> getCartItemsOfUser(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        List<CartProductsDto> cartProductsDtos = new ArrayList<>();
        cart.getProductsQuantity().forEach((productId, quantity) -> {
            ProductDto productDto = webClient.get()
                    .uri(INVENTORY_URL + "product/" + productId)
                    .retrieve()
                    .bodyToMono(ProductDto.class)
                    .block();
            cartProductsDtos.add(CartProductsDto.builder()
                    .price(productDto.getPrice())
                    .productId(productId)
                    .name(productDto.getName())
                    .image(productDto.getImage())
                    .quantity(quantity)
                    .build());
        });
        return ResponseEntity.ok(cartProductsDtos);
    }

    public ResponseEntity<?> addToCart(CartItemDto cartItemDto) {
        Cart cart = cartRepository.findByUserId(cartItemDto.getUserId());
        if (cart == null) {
            Map<String, Integer> productsQuantity = new HashMap<>();
            productsQuantity.put(cartItemDto.getProductId(), cartItemDto.getQuantity());
            cart = Cart.builder()
                    .userId(cartItemDto.getUserId())
                    .productsQuantity(productsQuantity)
                    .build();
            Cart newCart = cartRepository.save(cart);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newCart);
        }
        else {
            Map<String, Integer> productsQuantity = cart.getProductsQuantity();
            if (productsQuantity.containsKey(cartItemDto.getProductId())) {
                productsQuantity.put(cartItemDto.getProductId(),
                        productsQuantity.get(cartItemDto.getProductId()) + cartItemDto.getQuantity());
            }
            else {
                productsQuantity.put(cartItemDto.getProductId(), cartItemDto.getQuantity());
            }
            cart.setProductsQuantity(productsQuantity);
            Cart updatedCart = cartRepository.save(cart);
            return ResponseEntity.ok(updatedCart);
        }
    }

    public ResponseEntity<List<RemainingOrderDto>> getUnpackedOrders() {
        List<Order> unpackedOrders = OrderRepository.findByIsPackedFalse();

        List<RemainingOrderDto> remainingOrderDtos = unpackedOrders.stream()
                .map(order -> convertToRemainingOrderDto(order))
                .collect(Collectors.toList());

        return ResponseEntity.ok(remainingOrderDtos);
    }

    public String getProduct(String productId) {
        // Call inventory service to get product name based on productId
        return webClient.get()
                .uri(INVENTORY_URL + "product/" + productId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getUser(String userId) {
        // Call user service to get user name based on userId
        return webClient.get()
                .uri(USER_URL + userId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private RemainingOrderDto convertToRemainingOrderDto(Order order) {
        // Fetch user and product names
        String userName = getUser(order.getUser());
        String productName = getProduct(order.getProduct());

        // Create RemainingOrderDto
        return RemainingOrderDto.builder()
                .id(order.getId())
                .date(order.getDate())
                .customer(userName)
                .items(List.of(new RemainingOrderDto.Item(productName, order.getQuantity())))
                .isPacked(order.isPacked())
                .build();
    }

    public void getProduct(){

    }

    public void getUser(){

    }
}





