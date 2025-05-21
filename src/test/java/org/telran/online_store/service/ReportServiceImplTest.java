package org.telran.online_store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ReportServiceImplTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserJpaRepository userRepo;

    @Autowired
    private ProductJpaRepository productRepo;

    @Autowired
    private FavoriteJpaRepository favoriteRepo;

    @Autowired
    private OrderJpaRepository orderRepo;

    @Autowired
    private OrderItemJpaRepository orderItemRepo;

    private User adminUser;

    @BeforeEach
    void setup() {
        orderItemRepo.deleteAll();
        orderRepo.deleteAll();
        favoriteRepo.deleteAll();
        productRepo.deleteAll();
        userRepo.deleteAll();

        adminUser = User.builder()
                .name("Admin")
                .email("admin@example.com")
                .phone("0000000000")
                .password("adminpass")
                .role(UserRole.ADMINISTRATOR)
                .build();
        adminUser = userRepo.save(adminUser);
    }

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

        List<Product> topOrdered = reportService.getTopOrdered();
        assertEquals(1, topOrdered.size());
        assertEquals(product.getId(), topOrdered.get(0).getId());
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

        List<Product> topCancelled = reportService.getTopCancelled();
        assertEquals(1, topCancelled.size());
        assertEquals(product.getId(), topCancelled.get(0).getId());
    }

    @Test
    void testNotPaid() {
        Product product = createProduct("Unpaid Product");

        Order order = createOrder(OrderStatus.PAYMENT_PENDING);
        order.setUpdatedAt(LocalDateTime.now().minusDays(10));
        order = orderRepo.save(order);

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(1)
                .priceAtPurchase(product.getPrice())
                .build();
        orderItemRepo.save(item);
        orderItemRepo.flush();

        List<Product> result = reportService.getNotPaid(9L);

        assertEquals(1, result.size(), "One product should have been returned");
        assertEquals(product.getId(), result.get(0).getId(), "It's not the expected unpaid product");
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
                .user(adminUser)
                .build();
        return orderRepo.save(order);
    }
}