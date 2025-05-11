package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.telran.online_store.entity.CartItem;

import java.util.List;

@Builder
@Schema(description = "Cart")
public record CartResponseDto(

        @Schema(description = "Cart id", example = "1")
        Long cartId,

        @Schema(description = "List of cart items", example = "1")
        List<CartItemResponseDto> items
) {
}
