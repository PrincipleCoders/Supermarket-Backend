package com.principlecoders.common.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String id;
    private String userId;
    private String status;
    private Date date;
    private boolean isPacked;
    private List<OrderProduct> orderProducts;
}