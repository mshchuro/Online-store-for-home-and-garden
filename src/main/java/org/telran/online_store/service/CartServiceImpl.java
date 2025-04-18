package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.entity.CartItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.exception.CartItemNotFoundException;
import org.telran.online_store.exception.CartNotFoundException;
import org.telran.online_store.exception.ProductNotFoundException;
import org.telran.online_store.exception.UserNotFoundException;
import org.telran.online_store.repository.CartJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartJpaRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Override
    public Cart getCart(Long userId) {
        return Optional.ofNullable(cartRepository.findByUserId(userId))
                .orElseThrow(() ->
                        new CartNotFoundException("Cart is not found")
                );
    }

    @Override
    @Transactional
    public void addToCart(Long userId, AddToCartRequest request) {
        User user = userService.getById(userId);

        Product product = productService.getById(request.getProductId());

        Cart cart = cartRepository.findByUser(user).orElse(new Cart(null, user, new ArrayList<>()));
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst()
                .orElse(null);
        if (item == null) {
            cart.getItems().add(new CartItem(null, cart, product, request.getQuantity()));
        } else {
            item.setQuantity(request.getQuantity());
        }
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        User user = userService.getById(userId);
        Cart cart = cartRepository.findByUser(user).orElseThrow(()
                -> new CartNotFoundException("No such cart is found"));
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Cart item is not found"));
        if (item != null) {
            cart.getItems().remove(item);
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        User user = userService.getById(userId);
        Cart cart = cartRepository.findByUser(user).orElseThrow(()
                -> new CartNotFoundException("No such cart is found"));
        cartRepository.delete(cart);
    }
}
