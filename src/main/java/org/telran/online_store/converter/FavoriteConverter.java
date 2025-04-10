package org.telran.online_store.converter;

import org.springframework.stereotype.Component;
import org.telran.online_store.dto.FavoriteRequestDto;
import org.telran.online_store.dto.FavoriteResponseDto;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;

@Component
public class FavoriteConverter implements Converter<FavoriteRequestDto, FavoriteResponseDto, Favorite> {

    @Override
    public FavoriteResponseDto toDto(Favorite favorite) {
        return FavoriteResponseDto
                .builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .productId(favorite.getProduct().getId())
                .build();
    }

    @Override
    public Favorite toEntity(FavoriteRequestDto favoriteRequestDto) {
        User user = new User();
        user.setId(favoriteRequestDto.userId());

        Product product = new Product();
        product.setId(favoriteRequestDto.productId());

        return Favorite.builder()
                .user(user)
                .product(product)
                .build();
    }
}
