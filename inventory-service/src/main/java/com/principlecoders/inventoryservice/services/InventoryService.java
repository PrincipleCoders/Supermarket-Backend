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

    //Add product to inventory
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

    //update a product
    public ResponseEntity<Product> updateProduct(String id,Product product){
        Product updateProduct=productRepository.findById(id).orElse(null);
        updateProduct.setName(product.getName());
        updateProduct.setDescription(product.getDescription());
        updateProduct.setCategory(product.getCategory());

        productRepository.save(updateProduct);
        return ResponseEntity.ok(updateProduct);
    }



    //Get all products
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }
}
