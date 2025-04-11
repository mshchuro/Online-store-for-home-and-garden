package org.telran.online_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.converter.FavoriteConverter;
import org.telran.online_store.dto.FavoriteRequestDto;
import org.telran.online_store.dto.FavoriteResponseDto;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.service.FavoriteService;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class FavoriteController {

    private final FavoriteService favoriteService;

    private final Converter<FavoriteRequestDto, FavoriteResponseDto, Favorite> favoriteConverter;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, Converter<FavoriteRequestDto
            , FavoriteResponseDto, Favorite> favoriteConverter) {
        this.favoriteService = favoriteService;
        this.favoriteConverter = favoriteConverter;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponseDto>> getAll() {
        return ResponseEntity.ok(favoriteService.getAll().stream().map(favoriteConverter::toDto).toList());
    }

    @GetMapping("/favorites/{favorite_id}")
    public ResponseEntity<FavoriteResponseDto> getById(@PathVariable Long favorite_id) {
        Favorite favorite = favoriteService.getById(favorite_id);
        return ResponseEntity.ok(favoriteConverter.toDto(favorite));
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponseDto> create(@RequestBody FavoriteRequestDto requestDto) {
        Favorite favorite = favoriteConverter.toEntity(requestDto);
        Favorite saved = favoriteService.create(favorite);
        return ResponseEntity.ok(favoriteConverter.toDto(saved));
    }

    @DeleteMapping("/favorites/{favorite_id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long favorite_id) {
        favoriteService.delete(favorite_id);
        return ResponseEntity.accepted().build();
    }
}
