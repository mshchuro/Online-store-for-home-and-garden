package org.telran.online_store.service;

import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.Cart;


public interface CartService {

    Cart getCart();

    void addToCart(AddToCartRequest request);

    void removeFromCart(Long productId);

    void clearCart();
}