package org.telran.online_store.controller;

import jakarta.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.dto.UserUpdateRequest;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.service.ProductService;
import org.telran.online_store.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll(ServletRequest servletRequest) {
        return userService.getAll();
    }

    @PostMapping("/users/register")
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest updateRequest) {

        User updatedUser = userService.updateProfile(id, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
}