package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CartItemResponseDto(

        @Schema(description = "Product id", example = "1")
        Long productId,

        @Schema(description = "Quantity", example = "2")
        Integer quantity) {
}
