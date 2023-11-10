package com.principlecoders.orderservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


@Document(value = "orders")

public class Order {

    @Id

    private String id;
    private String orderNumber;
    private double totalAmount;

    @DBRef
    private Order customer;
}
