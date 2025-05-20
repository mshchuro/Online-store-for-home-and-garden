package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponseDto(

        @Schema(description = "Product id", example = "1")
        Long productId,

        @Schema(description = "Quantity", example = "2")
        Integer quantity,

        @Schema(description = "Total price for an amount of products", example = "15.22")
        BigDecimal priceAtPurchase) {
}