package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telran.online_store.entity.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

}
