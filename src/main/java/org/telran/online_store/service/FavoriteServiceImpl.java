package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.exception.FavoriteNotFoundException;
import org.telran.online_store.repository.FavoriteJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    FavoriteJpaRepository favoriteRepository;

    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    ProductJpaRepository productRepository;

    @Override
    public List<Favorite> getAll() {
        return favoriteRepository.findAll();
    }

    @Override
    @Modifying
    @Transactional
    public Favorite create(Favorite favorite) {
        userRepository.findById(favorite.get)
        return favoriteRepository.save(favorite);
    }

    @Override
    public Favorite getById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(()
                -> new FavoriteNotFoundException("Favorite product with id " + id + " is not found"));
    }

    @Override
    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }
}
