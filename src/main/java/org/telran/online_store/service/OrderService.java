package org.telran.online_store.service;

import org.aspectj.weaver.ast.Or;
import org.telran.online_store.dto.OrderRequestDto;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.Product;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getStatus(Long id);

    Order create(OrderRequestDto orderRequestDto);
}
