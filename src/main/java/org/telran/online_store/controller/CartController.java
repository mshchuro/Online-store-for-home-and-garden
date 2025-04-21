package org.telran.online_store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.dto.CartResponseDto;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.service.CartService;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

    private final CartService cartService;

    private final Converter<AddToCartRequest, CartResponseDto, Cart> cartConverter;

    public CartController(CartService cartService, Converter<AddToCartRequest, CartResponseDto, Cart> cartConverter) {
        this.cartService = cartService;
        this.cartConverter = cartConverter;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDto> getAllItems(@PathVariable Long userId) {
        return ResponseEntity.ok(cartConverter.toDto(cartService.getCart(userId)));
    }

    @PostMapping
    public ResponseEntity<Void> createCartItem(@RequestBody AddToCartRequest addToCartRequest) {
        cartService.addToCart(1L, addToCartRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId) {
        cartService.removeFromCart(1L, productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
