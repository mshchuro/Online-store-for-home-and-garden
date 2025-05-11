package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record FavoriteResponseDto(

        @Schema(description = "Favorite Product Id", example = "1")
        Long id,

        @Schema(description = "User Id", example = "1")
        Long userId,

        @Schema(description = "Product Id", example = "1")
        Long productId
) {
}
