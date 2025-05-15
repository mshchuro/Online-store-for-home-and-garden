package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;

import java.util.List;

@Repository
public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUser(User user);

    boolean existsByUserAndProduct(User user, Product product);

    void removeAllByProduct_Id(Long id);
}
