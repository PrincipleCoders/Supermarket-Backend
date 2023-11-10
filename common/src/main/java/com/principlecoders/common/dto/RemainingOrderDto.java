package com.principlecoders.common.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RemainingOrderDto {
    private String id;
    private Date date;
    private String userId;
    private String productId;
    private int quantity;
    private boolean isPacked;

}