package com.principlecoders.orderservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder



@Document(value = "orders")

public class Order {

    @Id

    private String id;
    private String orderNumber;
//    private double totalAmount;

    private List<OrderLineItems> orderLineItemsList;



//    @DBRef
//    private Order order;
}
