package org.telran.online_store.dto;

import lombok.Builder;

@Builder
public record FavoriteRequestDto(
        Long userId,
        Long productId
) {
}
