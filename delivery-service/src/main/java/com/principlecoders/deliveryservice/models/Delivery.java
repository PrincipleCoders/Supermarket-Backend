package com.principlecoders.deliveryservice.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery {
    private String id;
    private String orderId;
    private boolean markToDeliver;
    private boolean isDelivered;
    private String delivererId;
}
