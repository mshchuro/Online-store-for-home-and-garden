package org.telran.online_store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.FavoriteRequestDto;
import org.telran.online_store.dto.FavoriteResponseDto;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.User;
import org.telran.online_store.service.FavoriteService;
import org.telran.online_store.service.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final Converter<FavoriteRequestDto, FavoriteResponseDto, Favorite> favoriteConverter;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<FavoriteResponseDto>> getAll(Authentication authentication) {
        log.info("Authentication: {}", authentication.getName());

        User user = userService.getByEmail(authentication.getName());

        return ResponseEntity.ok(favoriteService.getAllByUser(user).stream().map(favoriteConverter::toDto).toList());
    }

    @GetMapping("/{favorite_id}")
    public ResponseEntity<FavoriteResponseDto> getById(@PathVariable Long favorite_id) {
        Favorite favorite = favoriteService.getById(favorite_id);
        return ResponseEntity.ok(favoriteConverter.toDto(favorite));
    }

    @PostMapping()
    public ResponseEntity<FavoriteResponseDto> create(@RequestBody FavoriteRequestDto requestDto,
                                                      Authentication authentication) {
        User user = userService.getByEmail(authentication.getName());

        Favorite favorite = favoriteConverter.toEntity(requestDto);
        favorite.setUser(user);
        Favorite saved = favoriteService.create(favorite);
        return ResponseEntity.ok(favoriteConverter.toDto(saved));
    }

    @DeleteMapping("/{favorite_id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long favorite_id, Authentication authentication) {
        Long currentUserId = userService.getByEmail(authentication.getName()).getId();
        Long userId = favoriteService.getById(favorite_id).getUser().getId();
        if (currentUserId.equals(userId)) {
            favoriteService.delete(favorite_id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
