package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserRegistrationResponse(

        @Schema(description = "User id", example = "1")
        Long id,

        @Schema(description = "Firstname, Lastname", example = "John Smith")
        String name,

        @Schema(description = "Email", example = "smith@mail.com")
        String email,

        @Schema(description = "Phone", example = "+49 000 000 000")
        String phone
) {
}
