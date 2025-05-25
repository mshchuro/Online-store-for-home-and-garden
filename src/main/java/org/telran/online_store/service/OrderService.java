package org.telran.online_store.service;

import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.enums.PeriodType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Order> getAll();

    List<Order> getAllUserOrders();

    Order getStatus(Long id);

    Order create(Order order);

    List<Order> getAllByStatus(OrderStatus orderStatus);

    void updateStatus(Long orderId, OrderStatus newStatus);

    Map<String, BigDecimal> getProfitReport(PeriodType periodType, Long periodAmount);

    List<OrderItem> getNotPaid(LocalDateTime dateTime);
}
