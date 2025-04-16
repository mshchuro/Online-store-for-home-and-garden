package org.telran.online_store.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.online_store.dto.FavoriteRequestDto;
import org.telran.online_store.dto.FavoriteResponseDto;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.service.ProductService;
import org.telran.online_store.service.UserService;

@Component
public class FavoriteConverter implements Converter<FavoriteRequestDto, FavoriteResponseDto, Favorite> {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

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
        User user = userService.getById(favoriteRequestDto.userId());
        Product product = productService.getById(favoriteRequestDto.productId());
        return Favorite.builder()
                .user(user)
                .product(product)
                .build();
    }
}
