package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequestDto(
        @NotBlank
        @Schema(description = "Product name", example = "Flower")
        String name,

        @Schema(description = "Product description", example = "Garden peony pink")
        String description,

        @Schema(description = "Product price", example = "15.55")
        BigDecimal price,

        @Schema(description = "discount", example = "1.55")
        BigDecimal discountPrice,

        @Schema(description = "Product categoryId", example = "1")
        Long categoryId,

        @Schema(description = "image Url", example = "/peony.png")
        String image) {

}
