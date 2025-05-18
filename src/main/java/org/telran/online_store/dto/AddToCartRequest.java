package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddToCartRequest {

    @NotNull(message = "Product id must not be null")
    @Schema(description = "Product id", example = "1")
    private Long productId;

    @Min(1)
    @Max(100)
    @Positive(message = "Quantity must be a positive number")
    @NotNull(message = "Product's quantity must not be null")
    @Schema(description = "Quantity", example = "1")
    private Integer quantity;
}
