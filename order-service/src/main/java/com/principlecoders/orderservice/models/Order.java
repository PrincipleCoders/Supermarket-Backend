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
    private Date date;
    private boolean isPacked;
    private List<OrderProduct> orderProducts;
}
