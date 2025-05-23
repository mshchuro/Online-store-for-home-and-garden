package org.telran.online_store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.entity.CartItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.exception.CartItemNotFoundException;
import org.telran.online_store.exception.CartNotFoundException;
import org.telran.online_store.repository.CartJpaRepository;

import java.util.HashSet;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartJpaRepository cartRepository;

    private final UserService userService;

    private final ProductService productService;

    @Override
    public Cart getCart() {
        Long currentUserId = userService.getCurrentUser().getId();
        return cartRepository.findByUserId(currentUserId)
                .orElseThrow(() ->
                        new CartNotFoundException("Cart is not found"));
    }

    @Override
    @Transactional
    public Cart addToCart(AddToCartRequest request) {
        User currentUser = userService.getCurrentUser();
        Product product = productService.getById(request.getProductId());

        Cart cart = cartRepository.findByUser(currentUser)
                .orElse(Cart.builder()
                        .items(new HashSet<>())
                        .user(currentUser)
                        .build());

        CartItem newItem = new CartItem(cart, product);

        if (cart.getItems().contains(newItem)) {
            CartItem existingItem = cart.getItems().stream()
                    .filter(i -> i.equals(newItem))
                    .findFirst()
                    .orElseThrow();
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            cart.getItems().add(new CartItem(cart, product, request.getQuantity()));
        }
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeFromCart(Long productId) {
        User user = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user).orElseThrow(()
                -> new CartNotFoundException("No such cart is found"));

        Product product = productService.getById(productId);
        CartItem newItem = new CartItem(cart, product);

        if (!cart.getItems().contains(newItem)) {
            throw new CartItemNotFoundException("Cart item is not found");
        }

        cart.getItems().remove(newItem);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart clearCart() {
        User user = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user).orElseThrow(()
                -> new CartNotFoundException("No such cart is found"));
        cart.setItems(new HashSet<>());
        return cartRepository.save(cart);
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }
}


