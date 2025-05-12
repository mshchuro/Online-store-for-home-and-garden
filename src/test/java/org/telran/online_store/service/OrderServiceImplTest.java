package org.telran.online_store.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.exception.OrderNotFoundException;
import org.telran.online_store.repository.FavoriteJpaRepository;
import org.telran.online_store.repository.OrderJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;

import java.math.BigDecimal;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderJpaRepository orderRepo;

    @Autowired
    private UserJpaRepository userRepo;

    @Autowired
    private ProductJpaRepository productRepo;

    @Autowired
    private FavoriteJpaRepository favoriteRepo;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Удаляем сначала зависимые сущности (favorites), потом пользователей
        favoriteRepo.deleteAll();    // Важно удалить перед users!
        productRepo.deleteAll();
        orderRepo.deleteAll();
        userRepo.deleteAll();

        // Создаём пользователя
        testUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .phone("1234567890")
                .password("password")
                .role(UserRole.CLIENT)
                .build();
        testUser = userRepo.save(testUser);

        // Создаём продукт
        Product testProduct = Product.builder()
                .name("Test Product")
                .description("Description")
                .price(BigDecimal.valueOf(100))
                .imageUrl("image.jpg")
                .build();
        testProduct = productRepo.save(testProduct);

        // Устанавливаем пользователя как аутентифицированного
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword(), List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testCreateOrder() {
        // Создание заказа
        Order order = Order.builder()
                .deliveryAddress("Some Street 123")
                .contactPhone("9876543210")
                .deliveryMethod("Courier")
                .status(OrderStatus.CREATED)
                .build();

        Order savedOrder = orderService.create(order);

        // Проверяем, что заказ сохранён и привязан к текущему пользователю
        assertNotNull(savedOrder.getId());
        assertEquals(testUser.getId(), savedOrder.getUser().getId());
    }

    @Test
    void testGetAllOrders() {
        // Создаём два заказа
        Order order1 = orderService.create(Order.builder()
                .deliveryAddress("Addr 1").contactPhone("123").deliveryMethod("Courier").status(OrderStatus.CREATED).build());

        Order order2 = orderService.create(Order.builder()
                .deliveryAddress("Addr 2").contactPhone("456").deliveryMethod("Pickup").status(OrderStatus.CREATED).build());

        List<Order> all = orderService.getAll();

        // Проверка, что оба заказа видны (в getAll возвращаются все, без фильтрации по пользователю)
        assertEquals(2, all.size());
    }

    @Test
    void testGetAllUserOrders() {
        // Создаём заказ от имени текущего пользователя
        orderService.create(Order.builder()
                .deliveryAddress("User Address").contactPhone("777").deliveryMethod("Courier").status(OrderStatus.CREATED).build());

        List<Order> userOrders = orderService.getAllUserOrders();

        // Проверяем, что заказ привязан к текущему пользователю
        assertEquals(1, userOrders.size());
        assertEquals(testUser.getId(), userOrders.get(0).getUser().getId());
    }

    @Test
    void testGetStatusThrowsExceptionForOtherUser() {
        // Создаём чужого пользователя с указанием роли
        User otherUser = User.builder()
                .name("Other User")
                .email("other@example.com")
                .phone("000")
                .password("pass")
                .role(UserRole.CLIENT)
                .build();
        otherUser = userRepo.save(otherUser);

        // Создаём заказ, принадлежащий другому пользователю
        Order foreignOrder = Order.builder()
                .deliveryAddress("Alien address").contactPhone("alien").deliveryMethod("Teleport").status(OrderStatus.CREATED)
                .user(otherUser)
                .build();
        foreignOrder = orderRepo.save(foreignOrder);

        // Пытаемся получить доступ — ожидаем AccessDeniedException
        Order finalForeignOrder = foreignOrder;
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> orderService.getStatus(finalForeignOrder.getId()));
    }

    @Test
    void testGetStatusThrowsIfNotFound() {
        // Проверка, что если заказ не найден, бросается исключение
        assertThrows(OrderNotFoundException.class, () -> orderService.getStatus(99999L));
    }
}