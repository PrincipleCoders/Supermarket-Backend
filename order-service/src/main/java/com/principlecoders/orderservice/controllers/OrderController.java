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

    @DeleteMapping("cart/user/{userId}/product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String userId, @PathVariable String productId) {
        return orderService.deleteCartItem(userId, productId);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<?> getOrderDetailsOfUser(@PathVariable String userId) {
        return orderService.getOrderDetailsOfUser(userId);
    }

    @GetMapping("remaining")
    public ResponseEntity<?> getRemainingOrders() {
        return orderService.getRemainingOrders();
    }

    @PutMapping("status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestBody boolean isPacked) {
        return orderService.updateOrderStatus(orderId, isPacked);
    }
}
