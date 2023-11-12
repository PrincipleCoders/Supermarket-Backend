package com.principlecoders.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemQuantity {
    private String item;
    private int quantity;
}
