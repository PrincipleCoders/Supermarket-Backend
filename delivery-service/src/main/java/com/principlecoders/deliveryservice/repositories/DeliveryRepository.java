package com.principlecoders.deliveryservice.repositories;

import com.principlecoders.deliveryservice.models.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends MongoRepository<Delivery, String> {
    // You can add custom queries here if needed
}
