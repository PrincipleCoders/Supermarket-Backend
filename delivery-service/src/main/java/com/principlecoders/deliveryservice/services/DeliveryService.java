package com.principlecoders.deliveryservice.services;

import com.principlecoders.deliveryservice.models.Delivery;
import com.principlecoders.deliveryservice.repositories.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public ResponseEntity<?> updateMarkToDeliverStatus(String deliveryId, boolean status) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);

        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setMarkToDeliver(status);
            return ResponseEntity.ok(deliveryRepository.save(delivery));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    public Delivery updateDelivered(String deliveryId, Boolean isDelivered) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);

        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setIsDelivered(isDelivered);
            return deliveryRepository.save(delivery);
        } else {
            return null; // Delivery not found
        }
    }

    public Delivery createDelivery(Delivery newDelivery) {
        return deliveryRepository.save(newDelivery);
    }
}
