package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telran.online_store.entity.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
