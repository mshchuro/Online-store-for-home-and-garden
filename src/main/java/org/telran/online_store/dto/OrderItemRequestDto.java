package org.telran.online_store.dto;

import lombok.Builder;

@Builder
public record OrderItemRequestDto(

        Long productId,

        int quantity
) {}