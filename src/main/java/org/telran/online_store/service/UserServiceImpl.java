package org.telran.online_store.service;

import org.springframework.stereotype.Service;
import org.telran.online_store.entity.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
