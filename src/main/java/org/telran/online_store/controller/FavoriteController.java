package org.telran.online_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.service.FavoriteService;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorites")
    public List<Favorite> getAll() {
        return favoriteService.getAll();
    }

    @GetMapping("/favorites/{favorite_id}")
    public Favorite getById(Long favorite_id) {
        return favoriteService.getById(favorite_id);
    }

    @PostMapping("/favorites")
    public Favorite create(@RequestBody Favorite favorite) {
        return favoriteService.create(favorite);
    }
}
