package com.principlecoders.inventoryservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
