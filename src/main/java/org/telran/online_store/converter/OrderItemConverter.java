package org.telran.online_store.converter;

import org.springframework.stereotype.Component;
import org.telran.online_store.dto.OrderItemRequestDto;
import org.telran.online_store.dto.OrderItemResponseDto;
import org.telran.online_store.entity.OrderItem;

@Component
public class OrderItemConverter implements Converter<OrderItemRequestDto, OrderItemResponseDto, OrderItem>{

    @Override
    public OrderItemResponseDto toDto(OrderItem orderItem) {
        return OrderItemResponseDto
                .builder()
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .priceAtPurchase(orderItem.getPriceAtPurchase())
                .build();
    }

    @Override
    public OrderItem toEntity(OrderItemRequestDto orderItemRequestDto) {
        return null;
    }
}