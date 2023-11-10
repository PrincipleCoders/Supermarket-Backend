package com.principlecoders.inventoryservice.services;

import com.principlecoders.inventoryservice.models.Product;
import com.principlecoders.inventoryservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public void createProduct(Product productRequest){
        Product product=Product.builder()
                .name(productRequest.getName())
                .description((productRequest.getDescription()))
                .category(productRequest.getCategory())
                .rating(productRequest.getRating())
                .supplier(productRequest.getSupplier())
               .price((productRequest.getPrice()))
                .quantity(productRequest.getQuantity())
                .image(productRequest.getImage())
                .build();

        productRepository.save(product);
        log.info("product {} is saved",product.getId());
    }
}
