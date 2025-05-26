package org.telran.online_store.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.telran.online_store.AbstractTests;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.entity.CartItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.repository.*;

import org.telran.online_store.repository.CartItemJpaRepository;
import org.telran.online_store.repository.CartJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@ActiveProfiles("test")
@Transactional
    public class CartServiceImplTest extends AbstractTests {
//
//    @Autowired
//    private CartService cartService;
//
//    @Autowired
//    private UserJpaRepository userRepo;
//
//    @Autowired
//    private ProductJpaRepository productRepo;
//
//    @Autowired
//    private CartJpaRepository cartRepo;
//
//    @Autowired
//    private OrderJpaRepository orderRepo;
//
//    @Autowired
//    private FavoriteJpaRepository favoriteRepo;
//
//    @Autowired
//    private CartItemJpaRepository cartItemRepo;
//
//    private User testUser;
//    private Product testProduct;
//
//    @BeforeEach
//    void setUp() {
//        orderRepo.deleteAll();
//        favoriteRepo.deleteAll();
//        cartItemRepo.deleteAll();
//        cartRepo.deleteAll();
//        productRepo.deleteAll();
//        userRepo.deleteAll();
//
//        testUser = User.builder()
//                .name("Test User")
//                .email("test@example.com")
//                .phone("1234567890")
//                .password("password")
//                .role(UserRole.CLIENT)
//                .build();
//        testUser = userRepo.save(testUser);
//
//        testProduct = Product.builder()
//                .name("Test Product")
//                .description("Description")
//                .price(BigDecimal.valueOf(100))
//                .imageUrl("image.jpg")
//                .build();
//        testProduct = productRepo.save(testProduct);
//
//        UsernamePasswordAuthenticationToken auth =
//                new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword(), List.of());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }

    @Test
    public void testAddToCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(testProduct.getId());
        request.setQuantity(2);

        cartService.addToCart(request);

        var cart = cartService.getCart();
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
      
        CartItem cartItem = cart.getItems().iterator().next();
        assertEquals(testProduct.getId(), cartItem.getProduct().getId());
        assertEquals(2, cartItem.getQuantity());
    }

    @Test
    public void testRemoveFromCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(testProduct.getId());
        request.setQuantity(2);
        cartService.addToCart(request);

        cartService.removeFromCart(testProduct.getId());

        var cart = cartService.getCart();
        assertNotNull(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testClearCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(testProduct.getId());
        request.setQuantity(1);
        cartService.addToCart(request);

        var cartBefore = cartService.getCart();
        assertFalse(cartBefore.getItems().isEmpty());

        cartService.clearCart();

        var cartAfter = cartService.getCart();
        assertTrue(cartAfter.getItems().isEmpty());
    }

    @Test
    public void testClearCartAfterAddingMultipleItems() {
        AddToCartRequest request1 = new AddToCartRequest();
        request1.setProductId(testProduct.getId());
        request1.setQuantity(2);

        AddToCartRequest request2 = new AddToCartRequest();
        request2.setProductId(testProduct.getId());
        request2.setQuantity(3);

        cartService.addToCart(request1);
        cartService.addToCart(request2);

        var cartBefore = cartService.getCart();
        assertFalse(cartBefore.getItems().isEmpty());

        cartService.clearCart();

        var cartAfter = cartService.getCart();
        assertTrue(cartAfter.getItems().isEmpty());
    }

    @Test
    public void testGetCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(testProduct.getId());
        request.setQuantity(3);
        cartService.addToCart(request);

        Cart cart = cartService.getCart();
        CartItem item = cart.getItems().iterator().next();

        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals(3, item.getQuantity());
    }
}