package org.telran.online_store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderItemRequestDto(

        @NotNull(message = "Product Id must not be blank")
        @Positive(message = "Product Id must be a positive number")
        Long productId,

        @NotNull(message = "Quantity must not be null")
        @Positive(message = "Quantity must be a positive number")
        int quantity
) {
}