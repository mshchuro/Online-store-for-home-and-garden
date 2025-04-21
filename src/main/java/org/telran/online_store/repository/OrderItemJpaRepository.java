package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telran.online_store.entity.OrderItems;

public interface OrderItemJpaRepository extends JpaRepository<OrderItems, Long> {
}
