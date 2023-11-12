package com.principlecoders.deliveryservice.repositories;

import com.principlecoders.deliveryservice.models.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends MongoRepository<Delivery, String> {
    List<Delivery> findAllByMarkToDeliverIsFalse();
}
