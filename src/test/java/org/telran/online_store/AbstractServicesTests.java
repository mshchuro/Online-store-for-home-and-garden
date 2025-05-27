package org.telran.online_store;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.repository.*;
import org.telran.online_store.service.*;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractServicesTests {

    @Autowired
    protected UserJpaRepository userRepo;

    @Autowired
    protected ProductJpaRepository productRepo;

    @Autowired
    protected CategoryJpaRepository categoryRepo;

    @Autowired
    protected CartJpaRepository cartRepo;

    @Autowired
    protected CartItemJpaRepository cartItemRepo;

    @Autowired
    protected OrderJpaRepository orderRepo;

    @Autowired
    protected OrderItemJpaRepository orderItemRepo;

    @Autowired
    protected FavoriteJpaRepository favoriteRepo;

    @Autowired
    protected FavoriteService favoriteService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected CartService cartService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected ReportService reportService;


    protected User testUser;
    protected Product testProduct;

    @BeforeEach
    @Transactional
    void setUp() {
        orderItemRepo.deleteAll();
        orderRepo.deleteAll();
        favoriteRepo.deleteAll();
        cartItemRepo.deleteAll();
        cartRepo.deleteAll();
        productRepo.deleteAll();
        userRepo.deleteAll();

        testUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .phone("1234567890")
                .password("password")
                .role(UserRole.CLIENT)
                .build();
        testUser = userRepo.save(testUser);

        testProduct = Product.builder()
                .name("Test Product")
                .description("Description")
                .price(BigDecimal.valueOf(100))
                .imageUrl("image.jpg")
                .build();
        testProduct = productRepo.save(testProduct);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword(), List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}