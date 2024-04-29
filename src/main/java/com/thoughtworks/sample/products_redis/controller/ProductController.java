package com.thoughtworks.sample.products_redis.controller;

import com.thoughtworks.sample.products_redis.domain.Product;
import com.thoughtworks.sample.products_redis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(products);
    }

    @GetMapping("/product")
    public ResponseEntity<Product> getProductById(
            @RequestParam(name = "productId") Long productId
    ) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product
    ) {
        Product productCreated = productService.createProduct(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productCreated);
    }

    @DeleteMapping("/product")
    public ResponseEntity<Product> deleteProduct(
            @RequestParam(name = "productId") Long productId
    ) {
        Product product = productService.removeProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}
