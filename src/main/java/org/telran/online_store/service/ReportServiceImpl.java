package org.telran.online_store.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.enums.PeriodType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private static final int PAGE_NUMBER = 0;

    private static final int PAGE_SIZE = 10;

    private final OrderService orderService;

    public ReportServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    public List<Product> getTopOrdered() {
        Pageable topTen = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<OrderItem> topFromOrders = orderService.getTopFromOrders(topTen, OrderStatus.DELIVERED);
        return topFromOrders
                .stream()
                .map(OrderItem::getProduct)
                .collect(Collectors.toList());
    }

    public List<Product> getTopCancelled() {
        Pageable topTen = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<OrderItem> topFromOrders = orderService.getTopFromOrders(topTen, OrderStatus.CANCELLED);
        return topFromOrders
                .stream()
                .map(OrderItem::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getNotPaid(Long days) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(days);
        return orderService.getNotPaid(thresholdDate)
                .stream()
                .map(OrderItem::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, BigDecimal> getProfitReport(PeriodType periodType, Long periodAmount) {
        return orderService.getProfitReport(periodType, periodAmount);
    }
}
