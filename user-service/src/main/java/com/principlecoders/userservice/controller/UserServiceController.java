package com.principlecoders.userservice.controller;


import com.principlecoders.common.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor

public class UserServiceController {

    private final RestTemplate restTemplate;


    @GetMapping("/viewProducts")
    public String viewProducts() {

        String products = restTemplate.getForObject("http://localhost:8088/allProduct" , String.class);
        System.out.println(products);
        return "User " + " views products: " + products;

    }
}
