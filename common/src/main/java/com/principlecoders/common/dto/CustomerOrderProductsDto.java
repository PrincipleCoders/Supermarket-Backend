package com.principlecoders.common.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOrderProductsDto {
    private String id;
    private Date date;
    private int bill;
    private List<ItemQuantity> items;
    private String customer;
    private String address;
    private String telephone;
    private boolean isDelivered;
    private boolean markToDeliver;
}
