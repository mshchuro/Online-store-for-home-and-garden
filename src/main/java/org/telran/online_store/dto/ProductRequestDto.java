package org.telran.online_store.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequestDto(
        String name,
        String description,
        BigDecimal price,
//        Long category,
        String image) {
}
