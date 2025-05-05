package org.telran.online_store.service;

import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.Product;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    List<Order> getAllUserOrders();

    Order getStatus(Long id);

    Order create(Order order);
}
