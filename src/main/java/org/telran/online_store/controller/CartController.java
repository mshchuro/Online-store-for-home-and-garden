package org.telran.online_store.controller;

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
@RequestMapping("/v1/cart")
public class CartController {

    private final CartService cartService;

    private final Converter<AddToCartRequest, CartResponseDto, Cart> cartConverter;

    @GetMapping()
    public ResponseEntity<CartResponseDto> getAllItems() {
        return ResponseEntity.ok(cartConverter.toDto(cartService.getCart()));
    }

    @PostMapping
    public ResponseEntity<Void> createCartItem(@RequestBody AddToCartRequest addToCartRequest) {
        cartService.addToCart(addToCartRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteCart() {
        cartService.clearCart();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
