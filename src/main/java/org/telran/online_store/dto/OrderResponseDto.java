package org.telran.online_store.dto;

import lombok.Builder;
import org.telran.online_store.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponseDto(

        Long id,

        Long userId,

        String contactPhone,

        String deliveryAddress,

        String deliveryMethod,

        OrderStatus status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        List<OrderItemResponseDto> items
) {}
