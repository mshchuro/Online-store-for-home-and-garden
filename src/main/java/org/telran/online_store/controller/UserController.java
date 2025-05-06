package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class UserController {

    private final UserService userService;

    private final UserRegistrationConverter userRegistrationConverter;

    private final Converter<UserUpdateRequestDto, UserUpdateResponseDto, User> userConverter;

    @Operation(
            summary = "Get all users",
            description = "Only user with role ADMINISTRATOR can view all the users. Authentication is required"
    )

    @GetMapping()
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping("/register")
    @Operation(
            summary = "New user registration",
            description = "Allows to register a new user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserRegistrationResponse.class))}),
            @ApiResponse(responseCode = "409", description = "User already exists", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ApiErrorResponse.class))})
    })
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