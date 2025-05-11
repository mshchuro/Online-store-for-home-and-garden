package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddToCartRequest {
    @NotBlank(message = "Product id must not be null")
    @Schema(description = "Product id", example = "1")
    private Long productId;

    @NotBlank(message = "Product's quantity must not be null")
    @Size(min = 1, message = "Quantity must be equal or greater then 1")
    @Schema(description = "Quantity", example = "1")
    private Integer quantity;
}
