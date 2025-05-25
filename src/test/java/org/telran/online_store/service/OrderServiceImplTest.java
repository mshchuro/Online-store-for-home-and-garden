package org.telran.online_store.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.telran.online_store.AbstractTests;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.DeliveryMethod;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.exception.OrderNotFoundException;
import java.util.List;

    class OrderServiceImplTest extends AbstractTests {

    @Test
    void testCreateOrder() {
        Order order = Order.builder()
                .deliveryAddress("Some Street 123")
                .contactPhone("9876543210")
                .deliveryMethod(DeliveryMethod.BY_CAR)
                .status(OrderStatus.CREATED)
                .build();

        Order savedOrder = orderService.create(order);

        assertNotNull(savedOrder.getId());
        assertEquals(testUser.getId(), savedOrder.getUser().getId());
    }

    @Test
    void testGetAllOrders() {
        Order order1 = orderService.create(Order.builder()
                .deliveryAddress("Addr 1").contactPhone("123").deliveryMethod(DeliveryMethod.BY_CAR).status(OrderStatus.CREATED).build());

        Order order2 = orderService.create(Order.builder()
                .deliveryAddress("Addr 2").contactPhone("456").deliveryMethod(DeliveryMethod.BY_PLANE).status(OrderStatus.CREATED).build());

        List<Order> all = orderService.getAll();

        assertEquals(2, all.size());
    }

    @Test
    void testGetAllUserOrders() {
        orderService.create(Order.builder()
                .deliveryAddress("User Address").contactPhone("777").deliveryMethod(DeliveryMethod.BY_CAR).status(OrderStatus.CREATED).build());

        List<Order> userOrders = orderService.getAllUserOrders();

        assertEquals(1, userOrders.size());
        assertEquals(testUser.getId(), userOrders.get(0).getUser().getId());
    }

    @Test
    void testGetStatusThrowsExceptionForOtherUser() {
        User otherUser = User.builder()
                .name("Other User")
                .email("other@example.com")
                .phone("000")
                .password("pass")
                .role(UserRole.CLIENT)
                .build();
        otherUser = userRepo.save(otherUser);

        Order foreignOrder = Order.builder()
                .deliveryAddress("Alien address").contactPhone("alien").deliveryMethod(DeliveryMethod.BY_CAR).status(OrderStatus.CREATED)
                .user(otherUser)
                .build();
        foreignOrder = orderRepo.save(foreignOrder);

        Order finalForeignOrder = foreignOrder;
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> orderService.getStatus(finalForeignOrder.getId()));
    }

    @Test
    void testGetStatusThrowsIfNotFound() {
        assertThrows(OrderNotFoundException.class, () -> orderService.getStatus(99999L));
    }
}
