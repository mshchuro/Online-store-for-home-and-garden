package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CategoryResponseDto(

        @Schema(description = "Category ID", example = "1")
        Long id,

        @Schema(description = "Category name", example = "Flowers")
        String name) {
}
