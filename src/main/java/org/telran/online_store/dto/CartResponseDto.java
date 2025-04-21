package org.telran.online_store.dto;

import lombok.Builder;
import org.telran.online_store.entity.CartItem;

import java.util.List;

@Builder
public record CartResponseDto(
        Long cartId,
        List<CartItemResponseDto> items
) {
}
