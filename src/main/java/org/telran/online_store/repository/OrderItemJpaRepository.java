package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

    void removeAllByProduct_Id(Long productId);

    @Query(value = """
        SELECT 
            CASE 
                WHEN :groupBy = 'HOUR' THEN to_char(o.updated_at, 'YYYY-MM-DD HH24:00')
                WHEN :groupBy = 'DAY' THEN to_char(o.updated_at, 'YYYY-MM-DD')
                WHEN :groupBy = 'WEEK' THEN to_char(date_trunc('week', o.updated_at), 'IYYY-IW')
                WHEN :groupBy = 'MONTH' THEN to_char(o.updated_at, 'YYYY-MM')
            END as period,
            SUM(oi.price_at_purchase * oi.quantity) as profit
        FROM order_items oi
        JOIN "orders" o ON oi.order_id = o.id
        WHERE o.status = 'DELIVERED' AND o.updated_at >= :fromDate
        GROUP BY period
        ORDER BY period
        """, nativeQuery = true)
    List<Object[]> getProfitData(@Param("fromDate") LocalDateTime fromDate,
                                 @Param("groupBy") String groupBy);
}
