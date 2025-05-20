package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequestDto(

        @NotBlank(message = "Product name must not be blank")
        @Size(min = 3, max = 255, message = "Product name must be at least 3 and at most 255 characters")
        @Schema(description = "Product name", example = "Flower")
        String name,

        @Size(max = 1000, message = "Description must be at most 1000 characters")
        @Schema(description = "Product description", example = "Garden peony pink")
        String description,

        @NotNull(message = "Price must not be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Price must be a valid monetary amount")
        @Schema(description = "Product price", example = "15.55")
        BigDecimal price,

        @DecimalMin(value = "0.0", message = "Discount must not be negative")
        @Digits(integer = 10, fraction = 2, message = "Discount must be a valid amount (maximum 2 fractional digits)")
        @Schema(description = "discount", example = "1.55")
        BigDecimal discountPrice,

        @Positive(message = "Category ID must be a positive number")
        @Schema(description = "Product categoryId", example = "1")
        Long categoryId,

        @Pattern(regexp = "^/.*\\.(png|jpg|jpeg|gif)$", message = "Image must be a valid image path")
        @Schema(description = "image Url", example = "/peony.png")
        String image) {
}
