package com.principlecoders.orderservice.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProduct {
    private String productId;
    private int quantity;
    private int price;
}
