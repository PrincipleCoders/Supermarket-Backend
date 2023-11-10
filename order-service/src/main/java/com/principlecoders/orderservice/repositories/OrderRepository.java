package com.principlecoders.orderservice.repositories;

import com.principlecoders.orderservice.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order,String> {
}
