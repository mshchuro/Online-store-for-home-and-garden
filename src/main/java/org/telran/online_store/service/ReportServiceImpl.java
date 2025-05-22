package org.telran.online_store.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EntityManager entityManager;

    private static final int PAGE_NUMBER = 0;

    private static final int PAGE_SIZE = 10;

    private final OrderService orderService;

    public ReportServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    public List<Product> getTopOrdered() {
        String sql = """
                SELECT
                prod.*,
                count(prod.id) AS quantity
                FROM products AS prod
                INNER JOIN public.order_items oi on prod.id = oi.product_id
                INNER JOIN public.orders o on o.id = oi.order_id AND o.status = 'DELIVERED'
                GROUP BY prod.id
                ORDER BY quantity DESC
                LIMIT 1;
                """;
        Query nativeQuery = entityManager.createNativeQuery(sql);

        Pageable topTen = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<Product> topFromOrders = orderService.getTopFromOrders(topTen, OrderStatus.DELIVERED);
        return topFromOrders;
//                .stream()
//                .map(OrderItem::getProduct)
//                .collect(Collectors.toList());
    }

    public List<Product> getTopCancelled() {
        Pageable topTen = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<Product> topFromOrders = orderService.getTopFromOrders(topTen, OrderStatus.CANCELLED);
        return topFromOrders;
//                .stream()
//                .map(OrderItem::getProduct)
//                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getNotPaid(Long days) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(days);
        return orderService.getNotPaid(thresholdDate)
                .stream()
                .map(OrderItem::getProduct)
                .collect(Collectors.toList());
    }
}
