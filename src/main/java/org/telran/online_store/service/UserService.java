package org.telran.online_store.service;

import org.telran.online_store.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User create(User user);

    User getById(Long id);

    void delete(Long id);

}
