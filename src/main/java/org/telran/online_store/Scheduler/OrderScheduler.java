package org.telran.online_store.Scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telran.online_store.entity.Order;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.repository.OrderJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderJpaRepository orderRepository;

    private final Random random = new Random();

    @Scheduled(fixedRate = 10_000)
    public void UpdateOrderStatus() {
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            OrderStatus currentStatus = order.getStatus();

            if (currentStatus == OrderStatus.CREATED || currentStatus == OrderStatus.PAYMENT_PENDING || currentStatus == OrderStatus.IN_DELIVERY) {
                OrderStatus newStatus = getRandomStatus(currentStatus);
                order.setStatus(newStatus);
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
                System.out.println("Order " + order.getId());
            }
        }
    }

    private OrderStatus getRandomStatus(OrderStatus currentStatus) {
        return switch (currentStatus) {
            case CREATED -> OrderStatus.PAYMENT_PENDING;
            case PAYMENT_PENDING -> random.nextBoolean() ? OrderStatus.IN_DELIVERY : OrderStatus.CANCELLED;
            case IN_DELIVERY -> OrderStatus.DELIVERED;
            default -> currentStatus;
        };
    }
}