package com.principlecoders.deliveryservice.controllers;

import com.principlecoders.deliveryservice.models.Delivery;
import com.principlecoders.deliveryservice.services.DeliveryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("delivery/")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping("markToDeliverStatus/{deliveryId}")
    public ResponseEntity<?> updateMarkToDeliverStatus(@PathVariable String deliveryId, @RequestBody boolean markToDeliver) {
        return deliveryService.updateMarkToDeliverStatus(deliveryId, markToDeliver);
    }

    @PutMapping("/updateDelivered/{deliveryId}")
    public ResponseEntity<Delivery> updateDelivered(
            @PathVariable String deliveryId,
            @RequestBody Map<String, Boolean> deliveredStatus) {

        Boolean isDelivered = deliveredStatus.get("isDelivered");

        Delivery updatedDelivery = deliveryService.updateDelivered(deliveryId, isDelivered);

        if (updatedDelivery != null) {
            return new ResponseEntity<>(updatedDelivery, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery newDelivery) {
        Delivery createdDelivery = deliveryService.createDelivery(newDelivery);

        if (createdDelivery != null) {
            return new ResponseEntity<>(createdDelivery, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
