package org.telran.online_store.dto;

import lombok.Builder;

@Builder
public record CartItemResponseDto(
        Long productId,
        Integer quantity
) {}
