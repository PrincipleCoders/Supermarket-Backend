package com.principlecoders.inventoryservice.services;

import com.principlecoders.inventoryservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ProductRepository productRepository;

//    public ResponseEntity<?> getCartItemsOfUser(List<String> productIds) {
//        if (productRepository.findAllById(productIds).isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(productRepository.findAllById(productIds));
//    }

    public ResponseEntity<?> getProductById(String productId) {
        if (productRepository.findById(productId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productRepository.findById(productId));
    }
}
