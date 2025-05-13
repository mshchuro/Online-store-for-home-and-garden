package org.telran.online_store.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telran.online_store.dto.UserRegistrationResponse;
import org.telran.online_store.handler.GlobalExceptionHandler;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Login", description = "API endpoint for login in application")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

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
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
