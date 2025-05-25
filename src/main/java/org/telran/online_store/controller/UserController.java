package org.telran.online_store.controller;

import jakarta.validation.Valid;
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
import org.telran.online_store.security.AuthenticationService;
import org.telran.online_store.security.SignInRequest;
import org.telran.online_store.security.SignInResponse;
import org.telran.online_store.service.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi{

    private final UserService userService;

    private final UserRegistrationConverter userRegistrationConverter;

    private final Converter<UserUpdateRequestDto, UserUpdateResponseDto, User> userConverter;

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping()
    @Override
    public ResponseEntity<List<UserUpdateResponseDto>> getAll() {
        List<User> users = userService.getAll();
        List<UserUpdateResponseDto> list = users.stream().map(userConverter::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/register")
    @Override
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        User user = userRegistrationConverter.toEntity(request);
        User savedUser = userService.create(user);
        if (log.isDebugEnabled()) {
            log.debug("User has been registered: {}", savedUser);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userRegistrationConverter.toDto(savedUser));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/{userId}")
    @Override
    public UserUpdateResponseDto getById(@PathVariable Long userId) {
        return userConverter.toDto(userService.getById(userId));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{userId}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{userId}")
    @Override
    public ResponseEntity<UserUpdateResponseDto> updateProfile(@PathVariable Long userId, @RequestBody UserUpdateRequestDto dto) {
        User user = userService.updateProfile(userId, userConverter.toEntity(dto));
        return ResponseEntity.ok(userConverter.toDto(user));
    }
}