package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User entity")
public record UserRegistrationRequest(

        @NotBlank
        @Schema(description = "Firstname, Lastname", example = "John Smith")
        String name,

        @NotBlank
        @Email
        @Schema(description = "Email", example = "smith@mail.com")
        String email,

        @NotBlank
        @Schema(description = "Phone", example = "+49 000 000 000")
        String phone,

        @NotBlank
        @Schema(description = "Password")
        String password) {
}
