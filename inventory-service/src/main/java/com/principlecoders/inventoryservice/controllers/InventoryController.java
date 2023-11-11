package com.principlecoders.inventoryservice.controllers;

import com.principlecoders.inventoryservice.models.Product;
import com.principlecoders.inventoryservice.repository.ProductRepository;
import com.principlecoders.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;


    @GetMapping("product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        return inventoryService.getProductById(productId);
    }

    @PostMapping("/addProduct")
    @ResponseStatus(HttpStatus.CREATED)

    public void createdProduct(@RequestBody Product productRequest){
        inventoryService.createProduct(productRequest);

    }
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id,@RequestBody Product product){
        return inventoryService.updateProduct(id,product);
    }

    @GetMapping("/allProduct")

    public List<Product> getAllProducts(){
     return    inventoryService.getAllProducts();
    }

//    @GetMapping("/allProduct")
//    public ResponseEntity<?> getProductById() {
//        return inventoryService.getAllProducts();
//    }
}
