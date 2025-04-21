package org.telran.online_store.converter;

import org.springframework.stereotype.Component;
import org.telran.online_store.dto.CartItemResponseDto;
import org.telran.online_store.entity.CartItem;

@Component
public class CartItemConverter implements Converter<CartItemResponseDto, CartItemResponseDto, CartItem> {
    @Override
    public CartItemResponseDto toDto(CartItem cartItem) {
        return CartItemResponseDto.builder()
                .productId(cartItem.getProduct().getId())
                .quantity(cartItem.getQuantity())
                .build();
    }

    @Override
    public CartItem toEntity(CartItemResponseDto cartItemResponseDto) {
        return null;
    }
}
