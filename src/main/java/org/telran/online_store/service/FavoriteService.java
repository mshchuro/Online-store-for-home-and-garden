package org.telran.online_store.service;

import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;

import java.util.List;

public interface FavoriteService {

    List<Favorite> getAll();

    Favorite create(Favorite favorite);

    Favorite getById(Long id);

    void delete(Long id);
}
