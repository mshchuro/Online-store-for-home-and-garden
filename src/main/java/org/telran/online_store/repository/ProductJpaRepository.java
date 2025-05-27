package org.telran.online_store.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.Product;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    List<Product> findAll(Specification<Product> spec, Sort sortObj);

    List<Product> findAllByCategory_Id(Long categoryId);

    @Query("""
                SELECT p FROM Product p
                WHERE p.discountPrice = (
                    SELECT MAX(p2.discountPrice) FROM Product p2 WHERE p2.discountPrice > 0
                )
            """)
    List<Product> productsWithDiscounts();

}
