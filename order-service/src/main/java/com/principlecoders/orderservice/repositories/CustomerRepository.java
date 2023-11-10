package com.principlecoders.orderservice.repositories;

import com.principlecoders.orderservice.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer,String> {
}
