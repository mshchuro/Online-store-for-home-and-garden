package org.telran.online_store.service;

import lombok.RequiredArgsConstructor;
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
import org.telran.online_store.repository.CartItemJpaRepository;
import org.telran.online_store.repository.CartJpaRepository;

import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartJpaRepository cartRepository;

    private CartItemJpaRepository cartItemRepository;

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
                .orElse(Cart.builder().items(new ArrayList<>())
                        .user(currentUser).build());
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst()
                .orElse(null);
        if (item == null) {
            cart.getItems().add(new CartItem(cart, product, request.getQuantity()));
        } else {
            Integer quantity = item.getQuantity();
            item.setQuantity(quantity + request.getQuantity());
        }
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeFromCart(Long productId) {
        User user = userService.getCurrentUser();
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

//    @Override
//    @Transactional
//    public void clearCart() {
//        User user = userService.getCurrentUser();
//        Cart cart = cartRepository.findByUser(user).orElseThrow(()
//                -> new CartNotFoundException("No such cart is found"));
//        cartRepository.delete(cart);
//    }


    @Override
    @Transactional
    public Cart clearCart() {
        User user = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user).orElseThrow(()
                -> new CartNotFoundException("No such cart is found"));
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);

 //       cartItemRepository.removeCartItemByCart_Id(cart.getId());
//        cart.getItems().clear();
//        return cart;
        //cartItemRepository.removeCartItemByCart_Id(cart.getId());
        //return cartRepository.save(cart);
    }
}


