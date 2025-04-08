package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.User;
import org.telran.online_store.exception.ProductNotFoundException;
import org.telran.online_store.exception.UserNotFoundException;
import org.telran.online_store.repository.UserJpaRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserJpaRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Modifying
    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException("User with id " + id + " is not found"));
    }

    @Override
    @Modifying
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateProfile(Long id, String name, String phone) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User with id " + id + " is not found"));

        user.setName(name);
        user.setPhone(phone);

        return userRepository.save(user);
    }
}
