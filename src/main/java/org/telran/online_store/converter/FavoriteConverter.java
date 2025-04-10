package org.telran.online_store.converter;

import org.telran.online_store.dto.FavoriteRequestDto;
import org.telran.online_store.dto.FavoriteResponseDto;
import org.telran.online_store.entity.Favorite;

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
        return Favorite
                .builder()
                .user(favoriteRequestDto.);
    }
}
