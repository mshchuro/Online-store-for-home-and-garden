package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.FavoriteRequestDto;
import org.telran.online_store.dto.FavoriteResponseDto;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.service.FavoriteService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Favorite Product", description = "API endpoints for favorite products. Authorisation is required for all end-points")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/favorites")
public class FavoriteController implements FavoriteApi{

    private final FavoriteService favoriteService;

    private final Converter<FavoriteRequestDto, FavoriteResponseDto, Favorite> favoriteConverter;

    @GetMapping
    @Override
    public ResponseEntity<List<FavoriteResponseDto>> getAll() {
        return ResponseEntity.ok(favoriteService.getAll()
                .stream()
                .map(favoriteConverter::toDto)
                .toList());
    }

    @PostMapping()
    @Override
    public ResponseEntity<FavoriteResponseDto> create(@Valid @RequestBody FavoriteRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteConverter.toDto(favoriteService.create(favoriteConverter.toEntity(requestDto))));
    }

    @DeleteMapping("/{favorite_id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long favorite_id) {
        favoriteService.delete(favorite_id);
        return ResponseEntity.ok().build();
    }
}
