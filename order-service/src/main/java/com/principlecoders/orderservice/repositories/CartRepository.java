package com.principlecoders.orderservice.repositories;

import com.principlecoders.orderservice.models.Cart;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByUserId(String userId);

    Order findAllByisPacked();
}
