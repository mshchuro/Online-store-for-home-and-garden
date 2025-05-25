package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.dto.UserRegistrationRequest;
import org.telran.online_store.dto.UserRegistrationResponse;
import org.telran.online_store.dto.UserUpdateRequestDto;
import org.telran.online_store.dto.UserUpdateResponseDto;
import org.telran.online_store.handler.GlobalExceptionHandler;
import org.telran.online_store.security.SignInRequest;
import org.telran.online_store.security.SignInResponse;
import java.util.List;

@Tag(name = "User management", description = "API endpoints for managing users")
@SecurityRequirement(name = "bearerAuth")
public interface UserApi {

    @Operation(
            summary = "Login"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = SignInResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler
                            .UnauthorizedErrorResponse.class))
            })
    })
    @PostMapping("/login")
    ResponseEntity<SignInResponse> login(@RequestBody SignInRequest request);

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
    ResponseEntity<List<UserUpdateResponseDto>> getAll();

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
    ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody UserRegistrationRequest request);

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
    UserUpdateResponseDto getById(@PathVariable Long userId);

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
    ResponseEntity<Void> deleteById(@PathVariable Long userId);

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
    ResponseEntity<UserUpdateResponseDto> updateProfile(@PathVariable Long userId, @RequestBody UserUpdateRequestDto dto);
}
