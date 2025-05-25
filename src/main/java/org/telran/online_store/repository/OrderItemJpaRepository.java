package org.telran.online_store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

    void removeAllByProduct_Id(Long productId);

//    @Query(value = """
//        SELECT
//            CASE
//                WHEN :groupBy = 'HOUR' THEN to_char(o.updated_at, 'YYYY-MM-DD HH24:00')
//                WHEN :groupBy = 'DAY' THEN to_char(o.updated_at, 'YYYY-MM-DD')
//                WHEN :groupBy = 'WEEK' THEN to_char(date_trunc('week', o.updated_at), 'IYYY-IW')
//                WHEN :groupBy = 'MONTH' THEN to_char(o.updated_at, 'YYYY-MM')
//            END as period,
//            SUM(oi.price_at_purchase * oi.quantity) as profit
//        FROM order_items oi
//        JOIN "orders" o ON oi.order_id = o.id
//        WHERE o.status = 'DELIVERED' AND o.updated_at >= :fromDate
//        GROUP BY period
//        ORDER BY period
//        """, nativeQuery = true)
//    List<Object[]> getProfitData(@Param("fromDate") LocalDateTime fromDate,
//                                 @Param("groupBy") String groupBy);


    @Query( value = """
    SELECT
    sum(sales.total),
    extract(months from sales.updated) AS month,
    extract(week from sales.updated) AS week,
    extract(days from sales.updated) AS days,
    extract(hours from sales.updated) AS hours

    FROM
            (SELECT
             t1.quantity * t1.price_at_purchase as total,
             t2.updated_at as updated
                     FROM order_items as t1
                     LEFT JOIN public.orders as t2 on t2.id = t1.order_id
                     WHERE t2.status = 'DELIVERED'
                     AND t2.updated_at > :fromDate) as sales
    GROUP BY month, week, days, hours""", nativeQuery = true )
    List<Object[]> getProfitData(@Param("fromDate") LocalDateTime fromDate);
}
