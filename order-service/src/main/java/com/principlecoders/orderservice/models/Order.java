package com.principlecoders.orderservice.models;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private String id;
    private String userId;
    private List<OrderProduct> orderProducts;
    private Date date;
    private String status;
}