package com.principlecoders.orderservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor


@Document(value = "orders")

public class Order {

    private String id;
    private String orderNumber;
    private double totalAmount;

    @DBRef
    private Customer customer;
}
