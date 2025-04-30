package org.telran.online_store.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductResponseDto(
        Long id,

        String name,

        String description,

        BigDecimal price,

        Long categoryId,

        String image,

        BigDecimal discountPrice,

        LocalDateTime createdAt,

        LocalDateTime updatedAt) {
}
