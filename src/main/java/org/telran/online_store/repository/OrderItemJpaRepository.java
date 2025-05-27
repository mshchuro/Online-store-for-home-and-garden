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
