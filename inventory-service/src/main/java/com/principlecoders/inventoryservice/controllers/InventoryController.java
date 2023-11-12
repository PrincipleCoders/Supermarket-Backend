package com.principlecoders.inventoryservice.controllers;

import com.principlecoders.common.dto.ProductDto;
import com.principlecoders.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("inventory/")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        return inventoryService.getProductById(productId);
    }

    @PostMapping("product")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        return inventoryService.addProduct(productDto);
    }

    @GetMapping("product/all")
    public ResponseEntity<?> getAllProducts() {
        return inventoryService.getAllProducts();
    }
}
