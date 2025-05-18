package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.dto.CartResponseDto;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.service.CartService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cart", description = "API endpoints to view cart items, add a product to cart, delete cart items, clear cart. Authorisation is required for all end-points")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/cart")
public class CartController implements CartApi{

    private final CartService cartService;

    private final Converter<AddToCartRequest, CartResponseDto, Cart> cartConverter;


    @GetMapping()
    @Override
    public ResponseEntity<CartResponseDto> getAllItems() {
        return ResponseEntity.ok(cartConverter.toDto(cartService.getCart()));
    }

    @PostMapping
    @Override
    public ResponseEntity<CartResponseDto> createCartItem(@Valid @RequestBody AddToCartRequest addToCartRequest) {
        Cart cart = cartService.addToCart(addToCartRequest);
        return ResponseEntity.status(HttpStatus.OK).body(cartConverter.toDto(cart));
    }

    @DeleteMapping("/{productId}")
    @Override
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping()
    @Override
    public ResponseEntity<CartResponseDto> deleteCart() {
        Cart cart = cartService.clearCart();
        return ResponseEntity.status(HttpStatus.OK).body(cartConverter.toDto(cart));
    }
}
