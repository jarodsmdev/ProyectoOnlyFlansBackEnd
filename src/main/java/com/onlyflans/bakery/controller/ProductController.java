package com.onlyflans.bakery.controller;

import com.onlyflans.bakery.model.Product;
import com.onlyflans.bakery.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        Product saved = productService.createProduct(product);
        URI location = URI.create("/products/" + saved.getCodigo());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
