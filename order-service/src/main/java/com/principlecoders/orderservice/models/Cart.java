package com.principlecoders.orderservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private String id;
    private String userId;
    private Map<String, Integer> productsQuantity;      //productId, quantity
}
