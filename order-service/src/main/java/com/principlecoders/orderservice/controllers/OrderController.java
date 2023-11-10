package com.principlecoders.orderservice.controllers;

import com.principlecoders.common.dto.CartItemDto;
import com.principlecoders.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("cart/user/{userId}")
    public ResponseEntity<?> getCartOfUser(@PathVariable String userId) {
        return orderService.getCartItemsOfUser(userId);
    }

    @PostMapping("cart")
    public ResponseEntity<?> addToCart(@RequestBody CartItemDto cartItemDto) {
        return orderService.addToCart(cartItemDto);
    }
    @DeleteMapping("cart/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        ResponseEntity<?> responseEntity = orderService.deleteCart(userId);
        return responseEntity;
    }

    @DeleteMapping("product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId){
        ResponseEntity<?> responseEntity = orderService.deleteCart(productId);
        return responseEntity;
    }
}
