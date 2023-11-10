package com.principlecoders.deliveryservice.models;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery {
    private String id;
    private String userId;
    private Boolean isMarked;
}
