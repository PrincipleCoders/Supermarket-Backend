package com.principlecoders.common.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RemainingOrderDto {
    private String id;
    private Date date;
    private String customer;
    private List<ItemQuantity> items;
    private boolean isPacked;
}
