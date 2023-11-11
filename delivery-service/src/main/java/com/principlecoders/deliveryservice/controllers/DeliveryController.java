package com.principlecoders.deliveryservice.controllers;

import com.principlecoders.deliveryservice.models.Delivery;
import com.principlecoders.deliveryservice.services.DeliveryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/deliveries")
@AllArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping("/updateMarkToDeliver/{deliveryId}")
    public ResponseEntity<Delivery> updateMarkToDeliver(
            @PathVariable String deliveryId,
            @RequestBody Map<String, Boolean> markStatus) {

        Boolean newMarkToDeliver = markStatus.get("markToDeliver");

        Delivery updatedDelivery = deliveryService.updateMarkToDeliver(deliveryId, newMarkToDeliver);

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
