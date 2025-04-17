package org.telran.online_store.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponseDto(

        String productName,

        Integer quantity,

        BigDecimal priceAtPurchase
) {}