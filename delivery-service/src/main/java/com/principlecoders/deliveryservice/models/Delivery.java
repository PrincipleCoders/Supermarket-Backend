package com.principlecoders.deliveryservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(value = "deliveries")

public class Delivery {
    @Id
    private String id;
    private Boolean markToDeliver;
    private Boolean isDelivered;
}
