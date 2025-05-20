package org.telran.online_store.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.dto.CartResponseDto;
import org.telran.online_store.entity.Cart;

@Component
@RequiredArgsConstructor
public class CartConverter implements Converter<AddToCartRequest, CartResponseDto, Cart> {

    private final CartItemConverter cartItemConverter;

    public CartResponseDto toDto(Cart cart) {
        return CartResponseDto.builder()
                .cartId(cart.getId())
                .items(cart.getItems().stream()
                        .map(cartItemConverter::toDto)
                        .toList())
                .build();
    }

    @Override
    public Cart toEntity(AddToCartRequest addToCartRequest) {
        throw new UnsupportedOperationException();
    }
}
