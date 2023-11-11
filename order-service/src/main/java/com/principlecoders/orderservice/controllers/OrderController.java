package com.principlecoders.orderservice.controllers;

import com.principlecoders.common.dto.CartItemDto;
import com.principlecoders.common.dto.UserRoleDto;
import com.principlecoders.orderservice.models.Order;
import com.principlecoders.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final RestTemplate restTemplate;
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

        return orderService.getAllOrdersByCustomers();

        }

        @PostMapping("/createOrders")
        @ResponseStatus(HttpStatus.CREATED)
        public String createOrder(@RequestBody Order order){

        orderService.placeOrder(order);

        return "Order place successfully";
        }


    //Call to userservice to get user details and combine those to order details
    @GetMapping("/userService/users")
    public String getUserDetails() {


        UserRoleDto users = restTemplate.getForObject("http://localhost:8085/userService/userInfo" , UserRoleDto.class);
        System.out.println(users);
        orderService.getUser(users);
        return  " views Users: " + users;
    }



}
