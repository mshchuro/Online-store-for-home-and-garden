package org.telran.online_store.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telran.online_store.dto.OrderRequestDto;
import org.telran.online_store.dto.OrderResponseDto;
import org.telran.online_store.entity.Order;
import org.telran.online_store.enums.OrderStatus;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class OrderConverter implements Converter<OrderRequestDto, OrderResponseDto, Order> {

    private final OrderItemConverter orderItemConverter;

    @Override
    public OrderResponseDto toDto(Order order) {

        return OrderResponseDto.builder()
                .id(order.getId())
                .items(order.getItems().stream().map(orderItemConverter::toDto).toList())
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
        Order order = Order.builder()
                .status(OrderStatus.CREATED)
                .deliveryAddress(dto.deliveryAddress())
                .deliveryMethod(dto.deliveryMethod())
                .contactPhone(dto.contactPhone())
                .items(new ArrayList<>())
                .build();

        dto.items().forEach(i -> order.addItem(orderItemConverter.toEntity(i)));
        return order;
    }
}
