package com.principlecoders.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductsDto {
    private String productId;
    private int quantity;
    private String name;
    private int price;
    private String image;
}
