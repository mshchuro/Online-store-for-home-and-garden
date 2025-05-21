package org.telran.online_store.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.entity.CartItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.exception.CartNotFoundException;
import org.telran.online_store.repository.CartItemJpaRepository;
import org.telran.online_store.repository.CartJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
public class CartServiceImplTest {

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private CartJpaRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private ProductJpaRepository productRepository;

    private User user;
    private Product product;
    @Autowired
    private CartItemJpaRepository cartItemJpaRepository;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhone("1234567890");
        user.setPassword("password123");
        userRepository.save(user);

        product = new Product();
        product.setName("Shovel");
        product.setPrice(new BigDecimal("15.50"));
        productRepository.save(product);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testAddToCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(product.getId());
        request.setQuantity(2);

        cartService.addToCart(request);

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    public void testRemoveFromCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(product.getId());
        request.setQuantity(2);
        cartService.addToCart(request);

        cartService.removeFromCart(product.getId());

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
        assertTrue(cart.getItems().isEmpty(), "Cart should be empty after removal");
    }

    @Test
    @Transactional
    public void testClearCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(product.getId());
        request.setQuantity(1);
        cartService.addToCart(request);

        Cart cartBefore = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        assertFalse(cartBefore.getItems().isEmpty());

        cartService.clearCart();

        Cart cartAfter = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found after clear"));

        assertTrue(cartAfter.getItems().isEmpty());
    }

    @Test
    @Transactional
    public void testClearCartAfterAddingMultipleItems() {
        AddToCartRequest request1 = new AddToCartRequest();
        request1.setProductId(product.getId());
        request1.setQuantity(2);

        AddToCartRequest request2 = new AddToCartRequest();
        request2.setProductId(product.getId());
        request2.setQuantity(3);

        cartService.addToCart(request1);
        cartService.addToCart(request2);

        Cart cartBeforeClear = cartService.getCart();
        int sizeBefore = cartBeforeClear.getItems().size();
        assertTrue(sizeBefore > 0, "Cart should have items before clearing");

        cartService.clearCart();

        Cart cartAfterClear = cartService.getCart();
        assertNotNull(cartAfterClear, "Cart should not be null after clearing");
        assertTrue(cartAfterClear.getItems().isEmpty(), "Cart should be empty after clearing");
    }

    @Test
    public void testGetCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(product.getId());
        request.setQuantity(3);
        cartService.addToCart(request);

        Cart cart = cartService.getCart();

        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
    }
}