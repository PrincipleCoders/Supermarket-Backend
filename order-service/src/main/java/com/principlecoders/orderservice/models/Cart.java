package com.principlecoders.orderservice.models;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    private String id;
    private String userId;
    private Map<String, Integer> productsQuantity;      //productId, quantity
}
