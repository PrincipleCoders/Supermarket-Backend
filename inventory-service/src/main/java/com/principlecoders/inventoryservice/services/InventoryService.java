package com.principlecoders.inventoryservice.services;

import com.principlecoders.common.dto.ProductDto;
import com.principlecoders.inventoryservice.models.Product;
import com.principlecoders.inventoryservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
public ResponseEntity<?> getAllProducts() {
    List<Product> products = productRepository.findAll();
    if (products.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(products);
}

    public ResponseEntity<?> getProductById(String productId) {
        if (productRepository.findById(productId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productRepository.findById(productId));
    }

    public ResponseEntity<?> addProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .image(productDto.getImage())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .rating(productDto.getRating())
                .supplier(productDto.getSupplier())
                .category(productDto.getCategory())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productRepository.save(product));
    }
}
