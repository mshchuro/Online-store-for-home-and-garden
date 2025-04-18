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
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAll(ServletRequest servletRequest) {
        return userService.getAll();
    }

    @PostMapping("/register")
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest updateRequest) {


        User updatedUser = userService.updateProfile(userId, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
}