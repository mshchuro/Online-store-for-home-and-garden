package org.telran.online_store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.CartItem;
import org.telran.online_store.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getAllItems(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getAllItems(userId));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody AddToCartRequest addToCartRequest) {
        cartService.addToCart(1L, addToCartRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@RequestParam Long userId, Long productId) {
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
