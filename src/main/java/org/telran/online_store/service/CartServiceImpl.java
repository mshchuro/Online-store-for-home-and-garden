package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.repository.CartJpaRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartJpaRepository cartRepository;

    @Override
    public void addToCart(Long userId, AddToCartRequest request) {

    }

    @Override
    public void removeFromCart(Long userId, Long productId) {

    }

    @Override
    public void clearCart(Long userId) {

    }
}
