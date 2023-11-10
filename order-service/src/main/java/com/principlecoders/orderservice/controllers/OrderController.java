package com.principlecoders.orderservice.controllers;

import com.principlecoders.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("cartItems/user/{userId}")
    public ResponseEntity<?> getCartOfUser(@PathVariable String userId) {
        return orderService.getCartItemsOfUser(userId);
    }
}
