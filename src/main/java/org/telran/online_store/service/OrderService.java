package org.telran.online_store.service;

import org.springframework.data.domain.Pageable;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    List<Order> getAll();

    List<Order> getAllUserOrders();

    Order getStatus(Long id);

    Order create(Order order);

    List<Order> getAllByStatus(OrderStatus orderStatus);

    void updateStatus(Long orderId, OrderStatus newStatus);

    List<OrderItem> getNotPaid(LocalDateTime dateTime);
}
