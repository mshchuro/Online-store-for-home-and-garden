package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
//@JsonIgnoreProperties(ignoreUnknown = false) Надо ли?
public record CategoryRequestDto(

        @NotBlank(message = "Category name must not be blank")
        @Size(min = 3, max = 255, message = "Category name must be at least 3 and at most 255 characters")
        @Schema(description = "Category name", example = "Flowers")
        String name) {
}
