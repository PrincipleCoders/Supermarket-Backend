package com.principlecoders.common.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductDto {
    private String id;
    private String productId;
    private String name;
    private int quantity;
    private int price;
    private String image;
    private LocalDateTime date;
}
