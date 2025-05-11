package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.dto.CartResponseDto;
import org.telran.online_store.dto.ProductResponseDto;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.handler.GlobalExceptionHandler;
import org.telran.online_store.service.CartService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cart", description = "API endpoints to view cart items, add a product to cart, delete cart items, clear cart. Authorisation is required for all end-points")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/cart")
public class CartController {

    private final CartService cartService;

    private final Converter<AddToCartRequest, CartResponseDto, Cart> cartConverter;

    @Operation(
            summary = "Allows to get a list of cart items",
            description = "Allows to view current user's cart items."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = CartResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))})
    })
    @GetMapping()
    public ResponseEntity<CartResponseDto> getAllItems() {
        return ResponseEntity.ok(cartConverter.toDto(cartService.getCart()));
    }

    @Operation(
            summary = "Adding a new item to cart",
            description = "Allows to add a new item to the cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = CartResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Not valid data", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<CartResponseDto> createCartItem(@RequestBody AddToCartRequest addToCartRequest) {
        Cart cart = cartService.addToCart(addToCartRequest);
        return ResponseEntity.status(HttpStatus.OK).body(cartConverter.toDto(cart));
    }

    @Operation(
            summary = "Removing item from cart",
            description = "Allows remove a product from cart."
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
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Clear cart",
            description = "Allows to remove all products from cart and the cart itself."
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
    @DeleteMapping()
    public ResponseEntity<Void> deleteCart() {
        cartService.clearCart();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
