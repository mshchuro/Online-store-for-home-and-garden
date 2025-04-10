package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.CartItem;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItem, Long> {
}
