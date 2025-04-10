package org.telran.online_store.service;

import org.telran.online_store.dto.AddToCartRequest;

public interface CartService {

    void addToCart(Long userId, AddToCartRequest request);

    void removeFromCart(Long userId, Long productId);

    void clearCart(Long userId);
}