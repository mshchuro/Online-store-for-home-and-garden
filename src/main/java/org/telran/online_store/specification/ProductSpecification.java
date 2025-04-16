package org.telran.online_store.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.telran.online_store.entity.Product;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> filterBy(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean discount
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if(categoryId != null){
                predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
            }

            if (minPrice != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (discount != null && discount) {
                predicate = cb.and(predicate,
                        cb.isNotNull(root.get("discountPrice")),
                        cb.lessThan(root.get("discountPrice"), root.get("price"))
                );
            }

            return predicate;
        };
    }
}
