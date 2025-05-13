package org.telran.online_store.security;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInRequest(
        @Schema(description = "email", example = "admin@example.com")
        String email,

        @Schema(description = "Product id", example = "password")
        String password) {
}
