package com.principlecoders.inventoryservice.controllers;

import com.principlecoders.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        return inventoryService.getProductById(productId);
    }
}
