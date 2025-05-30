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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.dto.ProductRequestDto;
import org.telran.online_store.dto.ProductResponseDto;
import org.telran.online_store.dto.ProductUpdateRequestDto;
import org.telran.online_store.handler.GlobalExceptionHandler;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Product management", description = "API endpoints for managing products")
@SecurityRequirement(name = "bearerAuth")
public interface ProductApi {

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
    ResponseEntity<List<ProductResponseDto>> getAll(
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
    );

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
    ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId);

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
    ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto dto);

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
    ResponseEntity<ProductResponseDto> update(@PathVariable Long productId, @Valid @RequestBody ProductUpdateRequestDto dto);

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
    ResponseEntity<Void> deleteById(@PathVariable Long productId);

    @Operation(
            summary = "Product of the day",
            description = "Shows a product with maximum discount"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    ResponseEntity<ProductResponseDto> getProductOfTheDay();
}
