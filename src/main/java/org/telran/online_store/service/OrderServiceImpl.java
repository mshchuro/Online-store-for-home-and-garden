package org.telran.online_store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.enums.PeriodType;
import org.telran.online_store.exception.OrderNotFoundException;
import org.telran.online_store.repository.OrderItemJpaRepository;
import org.telran.online_store.repository.OrderJpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderRepository;

    private final UserService userService;

    private final OrderItemJpaRepository orderItemRepository;

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
    public Map<String, BigDecimal> getProfitReport(PeriodType periodType, Long periodAmount) {
            LocalDateTime from = switch (periodType) {
                case HOUR -> LocalDateTime.now().minusHours(periodAmount);
                case DAY -> LocalDateTime.now().minusDays(periodAmount);
                case WEEK -> LocalDateTime.now().minusWeeks(periodAmount);
                case MONTH -> LocalDateTime.now().minusMonths(periodAmount);
            };

            List<Object[]> rawData = orderItemRepository.getProfitData(from);

            Map<String, BigDecimal> result = new LinkedHashMap<>();
            int index = 1;
            switch (periodType ) {
                case MONTH -> index = 1;
                case WEEK -> index = 2;
                case DAY -> index = 3;
                case HOUR -> index = 4;
            }
            for (Object[] row : rawData) {
                String key = String.valueOf(row[index]); // formatted date
                BigDecimal profit = (BigDecimal) row[0];
                result.put(key, result.getOrDefault(key, new BigDecimal(0)).add(profit));
            }

            return result;
    }

    @Override
    public List<OrderItem> getTopFromOrders(Pageable pageable, OrderStatus orderStatus) {
        return orderItemRepository.findTopByStatus(pageable, orderStatus);
    }

    @Override
    public List<OrderItem> getNotPaid(LocalDateTime dateTime) {
        return orderItemRepository.findNotPaidProducts(dateTime);
    }
}
