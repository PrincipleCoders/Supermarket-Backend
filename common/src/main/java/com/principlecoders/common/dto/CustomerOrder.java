package com.principlecoders.common.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOrder {
    private String id;
    private Date date;
    private String status;
    private int total;
    private int items;
    private String customer;
    private String address;
    private String telephone;
}
