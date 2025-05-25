package org.telran.online_store.controller;

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
