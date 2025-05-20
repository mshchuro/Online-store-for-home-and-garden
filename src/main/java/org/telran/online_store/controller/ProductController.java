package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Product management", description = "API endpoints for managing products")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/products")
public class ProductController implements ProductApi{

    private final ProductService productService;

    private final Converter<ProductRequestDto, ProductResponseDto, Product> productConverter;

    @GetMapping()
    @Override
    public ResponseEntity<List<ProductResponseDto>> getAll(
            @Parameter(description = "category ID for filtering")
            @RequestParam(required = false) Long categoryId,

            @Parameter(description = "minimum product price for filtering")
            @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "maximum product price for filtering")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "filter by availability of discount")
            @RequestParam(required = false) Boolean discount,

            @Parameter(description = "examples for sorting:\n price:asc, name:desc, " +
                                     "createdAt:desc, discountPrice:desc, etc.")
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
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long productId, @Valid @RequestBody ProductRequestDto dto) {
        Product product = productService.updateProduct(productId, productConverter.toEntity(dto));
        return ResponseEntity.ok(productConverter.toDto(product));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }
}
