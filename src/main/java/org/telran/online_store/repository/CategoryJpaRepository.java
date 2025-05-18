package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.Category;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);
}
