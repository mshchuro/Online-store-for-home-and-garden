package org.telran.online_store.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.enums.PeriodType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public List<ProductReportDto> getNotPaid(Long days) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(days);
        return getNotPaidForPeriodProducts(thresholdDate);
    }

    @Override
    public Map<String, BigDecimal> getProfitReport(PeriodType periodType, Long periodAmount) {
        return orderService.getProfitReport(periodType, periodAmount);
    }

    private List<ProductReportDto> getTopProductsByOrderStatus(OrderStatus orderStatus) {
        String sql = """
                SELECT
                prod.id, prod.name,
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
                .map(p -> new ProductReportDto((Long) p[0], (String) p[1])).toList();
    }

    private List<ProductReportDto> getNotPaidForPeriodProducts(LocalDateTime thresholdDate) {
        String sql = """
                SELECT
                prod.id, prod.name
                FROM products AS prod
                JOIN order_items oi ON prod.id = oi.product_id
                JOIN orders o ON o.id = oi.order_id
                WHERE o.status = 'PAYMENT_PENDING'
                  AND o.updated_at < :dateTime;
                """;

        Timestamp timestamp = Timestamp.valueOf(thresholdDate);
        Query nativeQuery = entityManager.createNativeQuery(sql).setParameter("dateTime", timestamp);
        List<Object[]> resultList = nativeQuery.getResultList();
        return resultList
                .stream()
                .map(p -> new ProductReportDto((Long) p[0], (String) p[1])).toList();
    }
}
