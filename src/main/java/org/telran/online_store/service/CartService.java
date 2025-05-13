package org.telran.online_store.service;

import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.entity.CartItem;

import java.util.List;

public interface CartService {

    Cart getCart();

    Cart addToCart(AddToCartRequest request);

    void removeFromCart(Long productId);

    Cart clearCart();
}