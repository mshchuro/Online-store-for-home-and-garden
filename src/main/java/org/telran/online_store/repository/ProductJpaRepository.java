package org.telran.online_store.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.telran.online_store.entity.Product;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    List<Product> findAll(Specification<Product> spec, Sort sortObj);
}
