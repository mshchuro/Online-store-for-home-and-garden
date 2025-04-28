package org.telran.online_store.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequestDto (

        List<OrderItemRequestDto> items,

        String deliveryAddress,

        String deliveryMethod,

        String contactPhone

        // Удалить
) {}
