package org.telran.online_store.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final EntityManager entityManager;

    private final OrderService orderService;

    public List<ProductReportDto> getTopOrdered() {
        return getTopProductsByOrderStatus(OrderStatus.DELIVERED);
    }

    public List<ProductReportDto> getTopCancelled() {
        return getTopProductsByOrderStatus(OrderStatus.CANCELLED);
    }

    @Override
    public List<Product> getNotPaid(Long days) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(days);
        return orderService.getNotPaid(thresholdDate)
                .stream()
                .map(OrderItem::getProduct)
                .collect(Collectors.toList());
    }

    private List<ProductReportDto> getTopProductsByOrderStatus(OrderStatus orderStatus) {
        String sql = """
                SELECT
                prod.*,
                count(prod.id) AS quantity
                FROM products AS prod
                INNER JOIN public.order_items oi on prod.id = oi.product_id
                INNER JOIN public.orders o on o.id = oi.order_id AND o.status = :status
                GROUP BY prod.id
                ORDER BY quantity DESC
                LIMIT 10;
                """;

        Query nativeQuery = entityManager.createNativeQuery(sql).setParameter("status", orderStatus.name());
        List<Object[]> resultList = nativeQuery.getResultList();
        return resultList
                .stream()
                .map(p -> new ProductReportDto((Long) p[0], (String) p[1], (Long) p[9])).toList();
    }
}
