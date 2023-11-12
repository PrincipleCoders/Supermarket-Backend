package com.principlecoders.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryDto {
    private String Id;
    private String orderId;
    private boolean markToDeliver;
    private boolean isDelivered;
    private String delivererId;
}
