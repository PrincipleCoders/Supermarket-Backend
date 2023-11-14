package com.principlecoders.deliveryservice.controllers;

import com.principlecoders.common.dto.DeliveryDto;
import com.principlecoders.deliveryservice.services.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("delivery/")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping("markToDeliverStatus/{orderId}/{status}")
    public ResponseEntity<?> updateMarkToDeliverStatus(@PathVariable String orderId, @PathVariable boolean status) {
        return deliveryService.updateMarkToDeliverStatus(orderId, status);
    }

    @PutMapping("delivered/{orderId}/{status}")
    public ResponseEntity<?> updateDelivered(@PathVariable String orderId, @PathVariable boolean status) {
        return deliveryService.updateDelivered(orderId, status);
    }

    @PostMapping("order/{orderId}")
    public ResponseEntity<?> createDelivery(@PathVariable String orderId) {
        return deliveryService.createDelivery(orderId);
    }

    @DeleteMapping("order/{orderId}")
    public ResponseEntity<?> deleteDelivery(@PathVariable String orderId) {
        return deliveryService.deleteDelivery(orderId);
    }

    @GetMapping("order/ready/all")
    public ResponseEntity<?> getAllReadyToDeliverOrders() {
        return deliveryService.getAllReadyToDeliverOrders();
    }
}
