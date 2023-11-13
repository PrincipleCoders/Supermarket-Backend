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

    @PutMapping("product")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto productDto) {
        return inventoryService.updateProduct(productDto);
    }

    @PutMapping("product/{productId}/quantity/decrease/{decrement}")
    public ResponseEntity<?> decreaseProductQuantity(@PathVariable String productId, @PathVariable int decrement) {
        System.out.println(decrement);
        return inventoryService.decreaseProductQuantity(productId, decrement);
    }

    @DeleteMapping("product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        return inventoryService.deleteProduct(productId);
    }
}
