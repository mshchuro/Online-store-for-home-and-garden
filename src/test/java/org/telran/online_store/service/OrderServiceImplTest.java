package org.telran.online_store.service;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telran.online_store.AbstractServicesTests;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.DeliveryMethod;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.exception.OrderNotFoundException;

import java.math.BigDecimal;
import java.util.List;

@Transactional
    class OrderServiceImplTest extends AbstractServicesTests {

    @BeforeEach
    void setUpCart() {
        AddToCartRequest addToCartRequest = new AddToCartRequest();
        addToCartRequest.setProductId(testProduct.getId());
        addToCartRequest.setQuantity(3);

        cartService.addToCart(addToCartRequest);
    }

    @Test
    void testCreateOrder() {
        Order order = Order.builder()
                .deliveryAddress("Some Street 123")
                .contactPhone("+9876543210")
                .deliveryMethod(DeliveryMethod.BY_CAR)
                .status(OrderStatus.CREATED)
                .build();
        List<OrderItem> orderItems = List.of(new OrderItem(null, order, testProduct, 3, new BigDecimal(33)));

        order.setItems(orderItems);
        Order savedOrder = orderService.create(order);

        assertNotNull(savedOrder.getId());
        assertEquals(testUser.getId(), savedOrder.getUser().getId());
    }

    @Test
    void testGetAllOrders() {
        Order order1 = Order.builder()
                .deliveryAddress("Addr 1")
                .contactPhone("123")
                .deliveryMethod(DeliveryMethod.BY_CAR)
                .status(OrderStatus.CREATED)
                .build();

        List<OrderItem> orderItemsOne = List.of(new OrderItem(null, order1, testProduct, 3, new BigDecimal(33)));
        order1.setItems(orderItemsOne);
        orderService.create(order1);

        Order order2 = Order.builder()
                .deliveryAddress("Addr 2")
                .contactPhone("456")
                .deliveryMethod(DeliveryMethod.BY_PLANE)
                .status(OrderStatus.CREATED)
                .build();
        List<OrderItem> orderItemsTwo = List.of(new OrderItem(null, order2, testProduct, 3, new BigDecimal(33)));
        order2.setItems(orderItemsTwo);
        orderService.create(order2);

        List<Order> allOrders = orderService.getAll();

        assertEquals(2, allOrders.size());
        assertEquals("Addr 1", allOrders.get(0).getDeliveryAddress());
        assertEquals("Addr 2", allOrders.get(1).getDeliveryAddress());
    }

    @Test
    void testGetAllUserOrders() {
        Order order = Order.builder()
                .deliveryAddress("User Address")
                .contactPhone("777")
                .deliveryMethod(DeliveryMethod.BY_CAR)
                .status(OrderStatus.CREATED)
                .build();

        List<OrderItem> orderItems = List.of(new OrderItem(null, order, testProduct, 3, new BigDecimal(33)));

        order.setItems(orderItems);
        orderService.create(order);
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
