package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Order;
import org.telran.online_store.exception.ProductNotFoundException;
import org.telran.online_store.repository.OrderJpaRepository;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderJpaRepository orderRepository;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    @Modifying
    @Transactional
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow(()
                -> new ProductNotFoundException("Order with id " + id + " is not found"));
    }

    @Override
    @Modifying
    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ProductNotFoundException("Order with id " + id + " not found");
        }
        orderRepository.deleteById(id);
    }
}
