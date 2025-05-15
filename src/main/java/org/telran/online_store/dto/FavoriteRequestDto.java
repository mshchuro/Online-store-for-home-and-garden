package org.telran.online_store.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record FavoriteRequestDto(

        @NotNull(message = "Product Id must not be blank")
        @Positive(message = "Product Id must be a positive number")
        Long productId
) {
}
