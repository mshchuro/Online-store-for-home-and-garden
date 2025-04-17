package org.telran.online_store.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telran.online_store.dto.OrderItemRequestDto;
import org.telran.online_store.dto.OrderItemResponseDto;
import org.telran.online_store.dto.OrderRequestDto;
import org.telran.online_store.dto.OrderResponseDto;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConverter implements Converter<OrderRequestDto, OrderResponseDto, Order>{


    @Override
    public OrderResponseDto toDto(Order order) {

        return OrderResponseDto.builder()
                .id(order.getOrderId())
                .status(order.getStatus())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryMethod(order.getDeliveryMethod())
                .contactPhone(order.getContactPhone())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    @Override
    public Order toEntity(OrderRequestDto dto) {
        return null;
//        Order.builder()
//                .items(dto.items())
//                .deliveryAddress(dto.deliveryAddress())
//                .deliveryMethod(dto.deliveryMethod())
//                .contactPhone(dto.contactPhone())
//                .build();
    }
}
