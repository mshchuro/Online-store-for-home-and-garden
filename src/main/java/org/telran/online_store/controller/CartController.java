package org.telran.online_store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.dto.CartResponseDto;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.entity.User;
import org.telran.online_store.service.CartService;
import org.telran.online_store.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cart")
public class CartController {

    private final CartService cartService;

    private final Converter<AddToCartRequest, CartResponseDto, Cart> cartConverter;

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<CartResponseDto> getAllItems(Authentication authentication) {
        User user = userService.getByEmail(authentication.getName());
        return ResponseEntity.ok(cartConverter.toDto(cartService.getCart(user.getId())));
    }

    @PostMapping
    public ResponseEntity<Void> createCartItem(@RequestBody AddToCartRequest addToCartRequest,
                                               Authentication authentication) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        cartService.addToCart(userId, addToCartRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId,
                                               Authentication authentication) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteCart(Authentication authentication) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
