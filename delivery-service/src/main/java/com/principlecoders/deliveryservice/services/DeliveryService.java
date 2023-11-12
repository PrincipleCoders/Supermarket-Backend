package com.principlecoders.deliveryservice.services;

import com.principlecoders.common.dto.DeliveryDto;
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

    public ResponseEntity<?> updateDelivered(String deliveryId, boolean isDelivered) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);

        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setDelivered(isDelivered);
            return ResponseEntity.ok(deliveryRepository.save(delivery));
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    public ResponseEntity<?> createDelivery(DeliveryDto deliveryDto) {
        Delivery newDelivery = deliveryRepository.save(Delivery.builder()
                .orderId(deliveryDto.getOrderId())
                .markToDeliver(deliveryDto.isMarkToDeliver())
                .isDelivered(deliveryDto.isDelivered())
                .delivererId(deliveryDto.getDelivererId())
                .build());

        if (newDelivery.getId() != null) {
            return ResponseEntity.ok(newDelivery);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
