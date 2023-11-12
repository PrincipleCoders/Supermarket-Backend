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

    @PutMapping("markToDeliverStatus/{deliveryId}")
    public ResponseEntity<?> updateMarkToDeliverStatus(@PathVariable String deliveryId, @RequestBody boolean markToDeliver) {
        return deliveryService.updateMarkToDeliverStatus(deliveryId, markToDeliver);
    }

    @PutMapping("deliveredStatus/{deliveryId}")
    public ResponseEntity<?> updateDelivered(@PathVariable String deliveryId, @RequestBody boolean isDelivered) {
        return deliveryService.updateDelivered(deliveryId, isDelivered);
    }

    @PostMapping("")
    public ResponseEntity<?> createDelivery(@RequestBody DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }
}
