package org.telran.online_store.service;

import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.User;

import java.util.List;

public interface FavoriteService {

    Favorite create(Favorite favorite);

    void delete(Long id);

    List<Favorite> getAll();
}
