package org.telran.online_store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.*;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.exception.OrderNotFoundException;
import org.telran.online_store.repository.OrderItemJpaRepository;
import org.telran.online_store.repository.OrderJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderRepository;

    private final OrderItemJpaRepository orderItemRepository;

    private final UserService userService;

    private final CartService cartService;

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

        Cart cart = cartService.getCart();
        Set<CartItem> cartItems = cart.getItems();

        List<OrderItem> orderItems = order.getItems();

        for (OrderItem orderItem : orderItems) {
            for (CartItem cartItem : cartItems) {
                if (orderItem.getProduct().equals(cartItem.getProduct())) {
                    cart.removeItem(cartItem);
                }
            }
        }

        order.setUser(currentUser);
        cartService.save(cart);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllByStatus(OrderStatus orderStatus) {
        return orderRepository.findAllByStatus(orderStatus);
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new OrderNotFoundException("Order with id " + orderId + " is not found"));
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    @Override
    public List<Product> getTopFromOrders(Pageable pageable, OrderStatus orderStatus) {
        return orderItemRepository.findTopByStatus(pageable, orderStatus);
    }

    @Override
    public List<OrderItem> getNotPaid(LocalDateTime dateTime) {
        return orderItemRepository.findNotPaidProducts(dateTime);
    }
}
