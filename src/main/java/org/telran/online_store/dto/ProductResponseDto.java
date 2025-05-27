package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Schema(description = "User entity")
public record ProductResponseDto(

        @Schema(description = "Product id", example = "1")
        Long id,

        @Schema(description = "Product name", example = "Flower")
        String name,

        @Schema(description = "Product description", example = "Garden peony pink")
        String description,

        @Schema(description = "Product price", example = "15.55")
        BigDecimal price,

        @Schema(description = "Product categoryId", example = "1")
        Long categoryId,

        @Schema(description = "image Url", example = "/peony.png")
        String image,

        @Schema(description = "discount", example = "1.55")
        BigDecimal discountPrice,

        @Schema(description = "time of the product has been created", example = "2025-05-08T17:09:43.856Z")
        LocalDateTime createdAt,

        @Schema(description = "time of the product has been updated", example = "2025-06-15T17:09:43.856Z")
        LocalDateTime updatedAt) {
}
