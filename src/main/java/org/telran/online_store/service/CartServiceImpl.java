package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.entity.CartItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
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
    private UserJpaRepository userRepository;

    @Autowired
    private ProductJpaRepository productRepository;

    @Override
    public List<CartItem> getAllItems(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("No user with id " + userId + " is found"));
        return cartRepository.findByUser(user)
                .map(cart -> cart.getItems().stream().toList())
                .orElse(List.of());
    }

    @Override
    public void addToCart(Long userId, AddToCartRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("No user with id " + userId + " is found"));

        Product product = productRepository.findById(request.getProductId()).orElseThrow(()
                -> new ProductNotFoundException("No product with id " + request.getProductId() + " is found"));

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
    public void removeFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("No user with id " + userId + " is found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()
                -> new CartNotFoundException("No such cart is found"));
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
        if (item != null) {
            cart.getItems().remove(item);
            cartRepository.save(cart);
        }
    }

    @Override
    public void clearCart(Long userId) {

    }
}
