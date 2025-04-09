package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telran.online_store.entity.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
}
