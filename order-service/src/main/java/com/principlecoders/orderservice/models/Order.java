package com.principlecoders.orderservice.models;import lombok.*;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private String id;
    private String userId;
    private int price;
    private Map<String, Integer> productsQuantity;
}
