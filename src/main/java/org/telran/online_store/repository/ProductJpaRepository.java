package org.telran.online_store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    List<Product> findAll(Specification<Product> spec, Sort sortObj);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByCategory_Id(Long categoryId);

    @Query("SELECT oi.product.name " +
           "FROM OrderItem oi " +
           "JOIN oi.order o " +
           "WHERE o.status = :status " +
           "GROUP BY oi.product.name " +
           "ORDER BY COUNT(oi) DESC")
    List<String> findTopTen(@Param(value = "status") String status, Pageable pageable);

    @Query("SELECT oi.product.name " +
           "FROM OrderItem oi " +
           "JOIN oi.order o " +
           "WHERE o.status = 'PAYMENT_PENDING' AND o.updatedAt < :thresholdDate " +
           "GROUP BY oi.product.name")
    List<String> findNotPaidProducts(@Param(value = "thresholdDate") LocalDateTime thresholdDate);
}
