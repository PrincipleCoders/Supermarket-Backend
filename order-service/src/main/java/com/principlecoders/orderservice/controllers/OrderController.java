package com.principlecoders.orderservice.controllers;

import com.principlecoders.common.dto.CartItemDto;
import com.principlecoders.orderservice.models.Order;
import com.principlecoders.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping( "/customerOrders")
        public List<Order>  getAllOrdersByCustomers(){

        System.out.println("gg");

        return orderService.getAllOrdersByCustomers();

        }

        @PostMapping("/createOrders")
    public void createOrder(@RequestBody Order order){
            System.out.println("hi");
        orderService.createOrder(order);
        }

}
