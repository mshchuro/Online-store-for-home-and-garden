package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import org.telran.online_store.handler.GlobalExceptionHandler;
import org.telran.online_store.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Product management", description = "API endpoints for managing products")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    private final Converter<ProductRequestDto, ProductResponseDto, Product> productConverter;

    @Operation(
            summary = "Allows to get a list of products",
            description = "Allows to filter products by category (categoryId), price range, discount \n" +
                          "and perform sorting by fields"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ProductResponseDto.class))})
    })
    @GetMapping()
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
            @RequestParam(required = false) List<String> sort
    ) {
        List<Product> products = productService.getAll(categoryId, minPrice, maxPrice, discount, sort);
        return ResponseEntity.ok(products.stream().map(productConverter::toDto).toList());
    }

    @Operation(
            summary = "Allows to get a products by it's id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ProductResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    @GetMapping("/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        return productConverter.toDto(productService.getById(productId));
    }

    @Operation(
            summary = "New product creating",
            description = "Allows to create a new product.\n Available only for Administrator"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ProductResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Not valid data", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "409", description = "Already exists", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotUniqueErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Product category is not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    @PostMapping()
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto dto) {
        Product product = productConverter.toEntity(dto);
        Product saved = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productConverter.toDto(saved));
    }

    @Operation(
            summary = "Product updating",
            description = "Allows to update product information such as name, description, price, discount, category and image url. Available only for Administrator"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ProductResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Not valid data", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Product category is not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long productId, @Valid @RequestBody ProductRequestDto dto) {
        Product product = productService.updateProduct(productId, productConverter.toEntity(dto));
        return ResponseEntity.ok(productConverter.toDto(product));
    }

    @Operation(
            summary = "Product deleting",
            description = "Allows to delete a product. Available only for Administrator"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteById(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }
}
