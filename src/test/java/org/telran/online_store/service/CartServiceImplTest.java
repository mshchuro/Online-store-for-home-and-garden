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
        // Создаем пользователя с паролем
        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhone("1234567890");
        user.setPassword("password123");  // Добавляем пароль
        userRepository.save(user);

        // Создаем продукт
        product = new Product();
        product.setName("Shovel");
        product.setPrice(new BigDecimal("15.50"));
        productRepository.save(product);

        // Подделываем аутентификацию пользователя для теста
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testAddToCart() {
        // Создаем запрос на добавление товара в корзину
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(product.getId());
        request.setQuantity(2);

        // Добавляем товар в корзину
        cartService.addToCart(request);

        // Получаем корзину текущего пользователя
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        // Проверяем, что товар был добавлен в корзину
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        CartItem cartItem = cart.getItems().get(0);
        assertEquals(product.getId(), cartItem.getProduct().getId());
        assertEquals(2, cartItem.getQuantity());
    }

    @Test
    public void testRemoveFromCart() {
        // Добавляем товар в корзину
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(product.getId());
        request.setQuantity(2);
        cartService.addToCart(request);

        // Удаляем товар из корзины
        cartService.removeFromCart(product.getId());

        // Получаем корзину и проверяем, что товар удален
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
        assertTrue(cart.getItems().isEmpty(), "Cart should be empty after removal");
    }

    @Test
    @Transactional
    public void testClearCart() {
        // Добавим товар в корзину
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(product.getId());
        request.setQuantity(1);
        cartService.addToCart(request);

        // Убедимся, что корзина не пуста
        Cart cartBefore = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        assertFalse(cartBefore.getItems().isEmpty());

        // Очищаем корзину
        cartService.clearCart();

        // ❗ Получаем корзину заново, чтобы избежать stale entities
        Cart cartAfter = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found after clear"));

        // Убеждаемся, что корзина теперь пуста
        assertTrue(cartAfter.getItems().isEmpty());
    }

    @Test
    @Transactional
    public void testClearCartAfterAddingMultipleItems() {
        // Arrange: добавляем товары в корзину
        AddToCartRequest request1 = new AddToCartRequest();
        request1.setProductId(product.getId());
        request1.setQuantity(2);

        AddToCartRequest request2 = new AddToCartRequest();
        request2.setProductId(product.getId());
        request2.setQuantity(3);

        cartService.addToCart(request1);
        cartService.addToCart(request2);

        // Act: получаем корзину до очистки и убеждаемся, что в ней есть товары
        Cart cartBeforeClear = cartService.getCart();
        int sizeBefore = cartBeforeClear.getItems().size();
        assertTrue(sizeBefore > 0, "Cart should have items before clearing");

        // Очищаем корзину
        cartService.clearCart();

        // Assert: получаем корзину заново и проверяем, что она пуста
        Cart cartAfterClear = cartService.getCart();
        assertNotNull(cartAfterClear, "Cart should not be null after clearing");
        assertTrue(cartAfterClear.getItems().isEmpty(), "Cart should be empty after clearing");
    }

    @Test
    public void testGetCart() {
        // Создаем корзину с продуктами для пользователя
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(product.getId());
        request.setQuantity(3);
        cartService.addToCart(request);

        // Получаем корзину пользователя
        Cart cart = cartService.getCart();

        // Проверяем, что корзина получена и содержит правильные данные
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).getQuantity());
    }
}