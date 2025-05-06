package org.telran.online_store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.exception.OrderNotFoundException;
import org.telran.online_store.repository.OrderJpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderRepository;

    private final UserService userService;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllUserOrders() {
        Long currentUserId = userService.getCurrentUser().getId();
        return orderRepository.findAllByUserId(currentUserId);
    }

    @Override
    public Order getStatus(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()
                -> new OrderNotFoundException("Order with id " + id + " is not found"));
        User currentUser = userService.getCurrentUser();
        if (!order.getUser().equals(currentUser)) {
            throw new AccessDeniedException("You are not allowed to access this order.");
        }
        return order;
    }

    @Override
    @Modifying
    @Transactional
    public Order create(Order order) {
        User currentUser = userService.getCurrentUser();
        order.setUser(currentUser);
        return orderRepository.save(order);
    }
}
