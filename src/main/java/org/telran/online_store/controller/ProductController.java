package org.telran.online_store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.ProductRequestDto;
import org.telran.online_store.dto.ProductResponseDto;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    private final Converter<ProductRequestDto, ProductResponseDto, Product> productConverter;

    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean discount,
            @RequestParam(required = false) List<String> sort
    ) {
        List<Product> products = productService.getAll(categoryId, minPrice, maxPrice, discount, sort);
        return ResponseEntity.ok(products.stream().map(productConverter::toDto).toList());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        Product product = productService.getById(productId);
        return ResponseEntity.ok(productConverter.toDto(product));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto dto) {
        Product product = productConverter.toEntity(dto);
        Product saved = productService.create(product);
        return ResponseEntity.ok(productConverter.toDto(saved));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long productId, @RequestBody ProductRequestDto dto) {
        Product product = productService.updateProduct(productId, productConverter.toEntity(dto));
        return ResponseEntity.ok(productConverter.toDto(product));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteById(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }
}
