package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.OrderItem;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

    void removeAllByProduct_Id(Long productId);
}
