package org.telran.online_store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

    void removeAllByProduct_Id(Long productId);

    @Query("SELECT oi " +
            "FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE o.status = :status " +
            "GROUP BY oi " +
            "ORDER BY COUNT (oi) DESC")
    List<OrderItem> findTopByStatus(Pageable pageable , OrderStatus status);

    @Query("SELECT oi " +
            "FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE o.status = 'PAYMENT_PENDING' AND o.updatedAt < :thresholdDate " +
            "GROUP BY oi")
    List<OrderItem> findNotPaidProducts(@Param(value = "thresholdDate") LocalDateTime thresholdDate);
}
