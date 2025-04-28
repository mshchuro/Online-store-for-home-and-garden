package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.dto.OrderRequestDto;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.Product;
import org.telran.online_store.exception.ProductNotFoundException;
import org.telran.online_store.repository.OrderJpaRepository;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderJpaRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getStatus(Long id) {
        return orderRepository.findById(id).orElseThrow(()
                -> new ProductNotFoundException("Order with id " + id + " is not found"));
    }

    @Override
    @Modifying
    @Transactional
    public Order create(OrderRequestDto orderRequestDto) {
//        Product product = productService.getById(orderRequestDto.items().);
//        return orderRepository.save(order);
        return null;
    }
}
