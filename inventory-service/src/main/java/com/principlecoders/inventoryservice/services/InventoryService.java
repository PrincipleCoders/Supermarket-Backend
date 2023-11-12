package com.principlecoders.inventoryservice.services;

import com.principlecoders.common.dto.ProductDto;
import com.principlecoders.inventoryservice.models.Product;
import com.principlecoders.inventoryservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ProductRepository productRepository;

    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

        Product newProduct = productRepository.save(product);
        if (newProduct.getId() != null) {
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateProductQuantity(String productId, int quantity) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        product.get().setQuantity(quantity);
        return ResponseEntity.ok(productRepository.save(product.get()));
    }
}
