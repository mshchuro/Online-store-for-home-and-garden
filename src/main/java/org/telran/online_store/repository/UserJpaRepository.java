package org.telran.online_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telran.online_store.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

}
