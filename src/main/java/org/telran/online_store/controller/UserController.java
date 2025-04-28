package org.telran.online_store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.converter.UserRegistrationConverter;
import org.telran.online_store.dto.*;
import org.telran.online_store.entity.User;
import org.telran.online_store.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserRegistrationConverter userRegistrationConverter;

    private final Converter<UserUpdateRequestDto, UserUpdateResponseDto, User> userConverter;

    @GetMapping()
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest request) {
        User user = userRegistrationConverter.toEntity(request);
        User savedUser = userService.create(user);
        if (log.isDebugEnabled()) {
            log.debug("User has been registered: {}", savedUser);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userRegistrationConverter.toDto(savedUser));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteById(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserUpdateResponseDto> updateProfile(@PathVariable Long userId, @RequestBody UserUpdateRequestDto dto) {
        User user = userService.updateProfile(userId, userConverter.toEntity(dto));
        return ResponseEntity.ok(userConverter.toDto(user));
    }
}