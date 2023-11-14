package com.principlecoders.common.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsDto {
    private String id;
    private Date date;
    private String status;
    private int total;
    private List<ItemQuantity> items;
}
