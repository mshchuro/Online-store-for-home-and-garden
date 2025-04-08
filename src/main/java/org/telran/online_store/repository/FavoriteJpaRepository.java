package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.online_store.entity.Favorite;

@Repository
public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {

}
