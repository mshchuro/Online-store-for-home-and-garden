package org.telran.online_store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.telran.online_store.AbstractTests;
import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.DeliveryMethod;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@ActiveProfiles("test")
class ReportServiceImplTest extends AbstractTests {
//
//    @Autowired
//    private ReportService reportService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private UserJpaRepository userRepo;
//
//    @Autowired
//    private ProductJpaRepository productRepo;
//
//    @Autowired
//    private FavoriteJpaRepository favoriteRepo;
//
//    @Autowired
//    private OrderJpaRepository orderRepo;
//
//    @Autowired
//    private OrderItemJpaRepository orderItemRepo;
//
//    private User testUser;
//    private Product testProduct;
//
//    @BeforeEach
//    void setUp() {
//        orderItemRepo.deleteAll();
//        orderRepo.deleteAll();
//        favoriteRepo.deleteAll();
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
    void testGetTopOrderedProducts() {
        Product product = createProduct("Ordered Product");

        Order order = createOrder(OrderStatus.DELIVERED);

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(5)
                .priceAtPurchase(product.getPrice())
                .build();
        orderItemRepo.save(item);

        List<ProductReportDto> topOrdered = reportService.getTopOrdered();
        assertEquals(1, topOrdered.size());
        assertEquals(product.getId(), topOrdered.get(0).id());
    }

    @Test
    void testGetTopCancelledProducts() {
        Product product = createProduct("Cancelled Product");

        Order order = createOrder(OrderStatus.CANCELLED);

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(3)
                .priceAtPurchase(product.getPrice())
                .build();
        orderItemRepo.save(item);

        List<ProductReportDto> topCancelled = reportService.getTopCancelled();
        assertEquals(1, topCancelled.size());
        assertEquals(product.getId(), topCancelled.get(0).id());
    }

    @Test
    void testNotPaid() {
        Product product = createProduct("Unpaid Product");

        Order order = createOrder(OrderStatus.PAYMENT_PENDING);
        order.setUpdatedAt(LocalDateTime.now().minusDays(11));
        order = orderRepo.save(order);

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(1)
                .priceAtPurchase(product.getPrice())
                .build();
        orderItemRepo.save(item);

        List<ProductReportDto> result = reportService.getNotPaid(1L);

        assertEquals(1, result.size(), "One product should have been returned");
        assertEquals(product.getId(), result.get(0).id(), "It's not the expected unpaid product");
    }

    private Product createProduct(String name) {
        Product product = Product.builder()
                .name(name)
                .description("desc")
                .price(BigDecimal.valueOf(100))
                .imageUrl("img.jpg")
                .build();
        return productRepo.save(product);
    }

    private Order createOrder(OrderStatus status) {
        Order order = Order.builder()
                .deliveryAddress("Street 1")
                .contactPhone("123456789")
                .deliveryMethod(DeliveryMethod.BY_CAR)
                .status(status)
                .user(testUser)
                .build();
        return orderRepo.save(order);
    }
}