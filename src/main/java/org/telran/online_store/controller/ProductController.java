package org.telran.online_store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.converter.ProductUpdateConverter;
import org.telran.online_store.dto.ProductRequestDto;
import org.telran.online_store.dto.ProductResponseDto;
import org.telran.online_store.dto.ProductUpdateRequestDto;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController implements ProductApi {

    private final ProductService productService;

    private final Converter<ProductRequestDto, ProductResponseDto, Product> productConverter;
    private final ProductUpdateConverter productUpdateConverter;

    @GetMapping()
    @Override
    public ResponseEntity<List<ProductResponseDto>> getAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean discount,
            @RequestParam(required = false) List<String> sort) {
        List<Product> products = productService.getAll(categoryId, minPrice, maxPrice, discount, sort);
        return ResponseEntity.ok(products.stream().map(productConverter::toDto).toList());
    }

    @GetMapping("/{productId}")
    @Override
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        Product product = productService.getById(productId);
        return ResponseEntity.ok(productConverter.toDto(product));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto dto) {
        Product product = productConverter.toEntity(dto);
        Product saved = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productConverter.toDto(saved));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long productId, @Valid @RequestBody ProductUpdateRequestDto dto) {
        Product product = productService.updateProduct(productId, productUpdateConverter.toEntity(dto));
        return ResponseEntity.ok(productUpdateConverter.toDto(product));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/product-of-the-day")
    public ResponseEntity<ProductResponseDto> getProductOfTheDay() {
        Product product = productService.getProductOfTheDay();
        return ResponseEntity.ok(productConverter.toDto(product));
    }
}
