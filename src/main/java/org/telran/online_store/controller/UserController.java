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
public class UserController {

    private final UserService userService;

    private final UserRegistrationConverter userRegistrationConverter;

    private final Converter<UserUpdateRequestDto, UserUpdateResponseDto, User> userConverter;

    @Operation(
            summary = "Allows to view all users' information",
            description = "Only user with role ADMINISTRATOR can view the information of all the users. Authorisation is required"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserRegistrationResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler
                            .UnauthorizedErrorResponse.class))
            })
    })
    @PreAuthorize("hasRole('ADMINISTRATOR')")

    @GetMapping()

    public ResponseEntity<List<UserUpdateResponseDto>> getAll() {
        List<User> users = userService.getAll();
        List<UserUpdateResponseDto> list = users.stream().map(userConverter::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "New user registration",
            description = "Allows to register a new user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserRegistrationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Not valid data", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        User user = userRegistrationConverter.toEntity(request);
        User savedUser = userService.create(user);
        if (log.isDebugEnabled()) {
            log.debug("User has been registered: {}", savedUser);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userRegistrationConverter.toDto(savedUser));
    }

    @Operation(
            summary = "Allows to view the user's information",
            description = "Only user with role ADMINISTRATOR can view the user's information. Authorisation is required"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserRegistrationResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler
                            .UnauthorizedErrorResponse.class))
            })
    })
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/{userId}")
    public UserUpdateResponseDto getById(@PathVariable Long userId) {
        return userConverter.toDto(userService.getById(userId));
    }

    @Operation(
            summary = "Allows to delete the user",
            description = "Only user with role ADMINISTRATOR can delete the user. Authorisation is required"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler
                            .UnauthorizedErrorResponse.class))
            })
    })
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Allows to update the user's info",
            description = "Only user with role ADMINISTRATOR can update the user's info. Authorisation is required"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserUpdateResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Not valid data", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler
                            .UnauthorizedErrorResponse.class))
                    })
    })
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserUpdateResponseDto> updateProfile(@PathVariable Long userId, @RequestBody UserUpdateRequestDto dto) {
        User user = userService.updateProfile(userId, userConverter.toEntity(dto));
        return ResponseEntity.ok(userConverter.toDto(user));
    }
}