package org.telran.online_store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderItemRequestDto(

        @NotBlank(message = "Product Id must not be blank")
        @Positive(message = "Product Id must be a positive number")
        Long productId,

        @NotBlank(message = "Quantity must not be blank")
        @Positive(message = "Quantity must be a positive number")
        int quantity
) {
}