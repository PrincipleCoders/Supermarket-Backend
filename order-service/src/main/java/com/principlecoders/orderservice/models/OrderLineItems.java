package com.principlecoders.orderservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "orderLineItems")

public class OrderLineItems {

    @Id
//    private Long id;
    private  String skuCode;
    private BigDecimal price;
    private Integer qauntity;
}
