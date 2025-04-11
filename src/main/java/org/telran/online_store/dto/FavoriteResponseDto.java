package org.telran.online_store.dto;

import lombok.Builder;

@Builder
public record FavoriteResponseDto(
        Long id,
        Long userId,
        Long productId
) {
}
