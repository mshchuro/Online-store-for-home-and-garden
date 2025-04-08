package org.telran.online_store.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.ProductRequestDto;
import org.telran.online_store.dto.ProductResponseDto;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ProductService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ProductController {

    private final ProductService productService;

    private final Converter<ProductRequestDto, ProductResponseDto, Product> productConverter;

    public ProductController(ProductService productService,
                             Converter<ProductRequestDto, ProductResponseDto, Product> productConverter) {
        this.productService = productService;
        this.productConverter = productConverter;
    }

    /// //
    @GetMapping("/products")
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/products/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        return productConverter.toDto(productService.getById(productId));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto dto) {
        Product product = productService.create(productConverter.toEntity(dto));
        if (log.isDebugEnabled()) {
            log.debug("Product created: {}", product);
        }
        return ResponseEntity.ok(productConverter.toDto(product));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.accepted().build();
    }
}
