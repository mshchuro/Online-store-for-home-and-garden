package org.telran.online_store.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telran.online_store.dto.FavoriteRequestDto;
import org.telran.online_store.dto.FavoriteResponseDto;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ProductService;

@RequiredArgsConstructor
@Component
public class FavoriteConverter implements Converter<FavoriteRequestDto, FavoriteResponseDto, Favorite> {

    private final ProductService productService;

    @Override
    public FavoriteResponseDto toDto(Favorite favorite) {
        return FavoriteResponseDto
                .builder()
                .id(favorite.getId())
                .productId(favorite.getProduct().getId())
                .build();
    }

    @Override
    public Favorite toEntity(FavoriteRequestDto favoriteRequestDto) {
        Product product = productService.getById(favoriteRequestDto.productId());
        return Favorite.builder()
                .product(product)
                .build();
    }
}
