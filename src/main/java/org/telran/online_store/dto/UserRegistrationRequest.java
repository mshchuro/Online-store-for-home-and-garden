package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
@Schema(description = "User entity")
public record UserRegistrationRequest(
        @Schema(description = "Firstname, Lastname", example = "John Smith")
        String name,

        @Schema(description = "Email", example = "smith@mail.com")
        String email,

        @Schema(description = "Phone", example = "+49 000 000 000")
        String phone,

        @Schema(description = "Password")
        String password
) {
}
