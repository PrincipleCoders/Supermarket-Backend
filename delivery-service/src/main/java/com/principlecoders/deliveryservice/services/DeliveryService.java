package com.principlecoders.deliveryservice.services;

import com.principlecoders.deliveryservice.models.Delivery;
import com.principlecoders.deliveryservice.repositories.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public Delivery updateMarkToDeliver(String deliveryId, Boolean newMarkToDeliver) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);

        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setMarkToDeliver(newMarkToDeliver);
            return deliveryRepository.save(delivery);
        } else {
            return null; // Delivery not found
        }
    }

    public Delivery createDelivery(Delivery newDelivery) {
        return deliveryRepository.save(newDelivery);
    }
}
