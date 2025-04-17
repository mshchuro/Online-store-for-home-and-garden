package org.telran.online_store.service;

import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.CartItem;

import java.util.List;

public interface CartService {

    List<CartItem> getAllItems(Long userId);

    void addToCart(Long userId, AddToCartRequest request);

    void removeFromCart(Long userId, Long productId);

    void clearCart(Long userId);
}