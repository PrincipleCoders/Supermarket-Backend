package com.principlecoders.orderservice.repositories;

import com.principlecoders.orderservice.models.Cart;
import com.principlecoders.orderservice.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByuserId(String userId);
}
