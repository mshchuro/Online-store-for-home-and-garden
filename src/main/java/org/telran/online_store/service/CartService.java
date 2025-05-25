package org.telran.online_store.service;

import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.Cart;

public interface CartService {

    Cart getCart();

    Cart addToCart(AddToCartRequest request);

    void removeFromCart(Long productId);

    Cart clearCart();

    Cart save(Cart cart);
}