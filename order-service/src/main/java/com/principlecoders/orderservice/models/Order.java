package com.principlecoders.orderservice.models;

import lombok.*;

import java.util.Map;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private String id;
    private String userId;
    private LocalDateTime date;
    private Map<String, Integer> productsQuantity;
}
