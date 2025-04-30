package org.telran.online_store.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponseDto(

        Long productId,

        Integer quantity,

        BigDecimal priceAtPurchase
) {}