package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;
import org.telran.online_store.repository.FavoriteJpaRepository;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    FavoriteJpaRepository favoriteRepository;

    @Override
    public List<Favorite> getAll() {
        return favoriteRepository.findAll();
    }

    @Override
    @Modifying
    @Transactional
    public Favorite create(Favorite favorite) {

        return favoriteRepository.save(favorite);
    }

    @Override
    public Favorite getById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
