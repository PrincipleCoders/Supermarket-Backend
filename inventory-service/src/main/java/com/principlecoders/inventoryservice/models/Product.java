package com.principlecoders.inventoryservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(value = "products")
@Builder
public class Product {
    @Id
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
