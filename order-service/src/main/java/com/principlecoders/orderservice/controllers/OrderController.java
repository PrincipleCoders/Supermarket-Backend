package com.principlecoders.orderservice.controllers;

import com.principlecoders.common.dto.CartItemDto;
import com.principlecoders.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("cart/user/{userId}")
    public ResponseEntity<?> getCartOfUser(@PathVariable String userId) {
        return orderService.getCartItemsOfUser(userId);
    }

    @PutMapping("cart")
    public ResponseEntity<?> addToCart(@RequestBody CartItemDto cartItemDto) {
        return orderService.addOrUpdateCart(cartItemDto);
    }

    @DeleteMapping("cart/user/{userId}/product/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable String userId, @PathVariable String productId) {
        return orderService.deleteCartItem(userId, productId);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<?> getOrderDetailsOfUser(@PathVariable String userId) {
        return orderService.getOrderDetailsOfUser(userId);
    }

    @GetMapping("remaining/all")
    public ResponseEntity<?> getRemainingOrders() {
        return orderService.getRemainingOrders();
    }

    @PutMapping("{orderId}/isPacked/{isPacked}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @PathVariable boolean isPacked) {
        return orderService.updateOrderStatus(orderId, isPacked);
    }

    @GetMapping("user/all")
    public ResponseEntity<?> getAllOrdersOfUsers() {
        return orderService.getAllOrdersOfUsers();
    }

    @PostMapping("checkout/user/{userId}")
    public ResponseEntity<?> checkout(@PathVariable String userId) {
        return orderService.checkout(userId);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }
}
