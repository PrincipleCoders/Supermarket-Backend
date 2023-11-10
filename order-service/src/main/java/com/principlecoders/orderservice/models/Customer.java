package com.principlecoders.orderservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document( value = "customer")


public class Customer {

    private String id;

    private String firstName;
    private  String lastName;
}
