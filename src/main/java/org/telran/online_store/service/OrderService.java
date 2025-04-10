package org.telran.online_store.service;

import org.aspectj.weaver.ast.Or;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.Product;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order create(Order order);

    Order getById(Long id);

    void delete(Long id);
}
