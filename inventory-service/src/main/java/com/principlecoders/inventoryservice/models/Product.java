package com.principlecoders.inventoryservice.models;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private String id;
    private String name;
    private String description;
    private String category;
    private Double rating;
    private String supplier;
    private int price;
    private int quantity;
    private String image;
}
