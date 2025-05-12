package org.telran.online_store.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telran.online_store.entity.Order;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.service.OrderService;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderService orderService;

    private final Random random = new Random();

    @Async("pool")
    @Scheduled(fixedRate = 10000)
    public void setPendingStatus() {
        List<Order> allOrders = orderService.getAllByStatus(OrderStatus.CREATED);
        for (Order order : allOrders) {
            orderService.updateStatus(order.getId(), OrderStatus.PAYMENT_PENDING);
        }
    }

    @Async("pool")
    @Scheduled(fixedRate = 10000)
    public void setStatusToPaidOrCancelled() {
        List<Order> allOrders = orderService.getAllByStatus(OrderStatus.PAYMENT_PENDING);
        for (Order order : allOrders) {
            OrderStatus orderStatus = random.nextBoolean() ? OrderStatus.PAID : OrderStatus.CANCELLED;
            orderService.updateStatus(order.getId(), orderStatus);
        }
    }

    @Async("pool")
    @Scheduled(fixedRate = 10000)
    public void setStatusToInDelivery() {
        List<Order> allOrders = orderService.getAllByStatus(OrderStatus.PAID);
        for (Order order : allOrders) {
            orderService.updateStatus(order.getId(), OrderStatus.IN_DELIVERY);
        }
    }

    @Async("pool")
    @Scheduled(fixedRate = 10000)
    public void setStatusToDelivered() {
        List<Order> allOrders = orderService.getAllByStatus(OrderStatus.IN_DELIVERY);
        for (Order order : allOrders) {
            orderService.updateStatus(order.getId(), OrderStatus.DELIVERED);
        }
    }
}