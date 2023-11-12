package com.principlecoders.common.dto;

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
