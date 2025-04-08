package org.telran.online_store.controller;

import jakarta.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.ProductDto;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class ProductController {

    private final ProductService productService;

    private final Converter<ProductDto, ProductDto, Product> productConverter;

    public ProductController(ProductService productService,
                             Converter<ProductDto, ProductDto, Product> productConverter) {
        this.productService = productService;
        this.productConverter = productConverter;
    }

    /// //
    @GetMapping("/products")
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/products/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) {
        return productConverter.toDto(productService.getById(productId));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> create(@RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.create(productConverter.toEntity(dto)));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.accepted().build();
    }
}
