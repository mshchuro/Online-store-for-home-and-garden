package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.telran.online_store.exception.UserNotUniqueException;
import org.telran.online_store.handler.GlobalExceptionHandler;
import org.telran.online_store.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User management", description = "API endpoints for managing users")
@SecurityRequirement(name = "bearerAuth")
public class UserController implements UserApi{

    private final UserService userService;

    private final UserRegistrationConverter userRegistrationConverter;

    private final Converter<UserUpdateRequestDto, UserUpdateResponseDto, User> userConverter;

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