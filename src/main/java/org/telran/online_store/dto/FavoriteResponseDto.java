package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FavoriteResponseDto(

        @Schema(description = "Favorite Product Id", example = "1")
        Long id,

        @Schema(description = "Product Id", example = "1")
        Long productId) {
}
