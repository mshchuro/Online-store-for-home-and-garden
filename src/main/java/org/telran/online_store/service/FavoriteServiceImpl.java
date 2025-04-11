package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.exception.*;
import org.telran.online_store.repository.FavoriteJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;

import java.util.List;
import java.util.Optional;

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
        return favoriteRepository.findAll(); //getAllByUser
    }

    @Override
    @Modifying
    @Transactional
    public Favorite create(Favorite favorite) {
        Optional.ofNullable(favorite.getUser()).map(User::getId).ifPresent(id -> {
            User user = userRepository.findById(id).orElseThrow(
                    () -> new UserNotFoundException("User with id " + id + " is not found"));
            favorite.setUser(user);
        });

        Optional.ofNullable(favorite.getProduct()).map(Product::getId).ifPresent(id -> {
            Product product = productRepository.findById(id).orElseThrow(
                    () -> new ProductNotFoundException("Product with id " + id + " is not found"));
            favorite.setProduct(product);
        });
        boolean exists = favoriteRepository.existsByUserAndProduct(favorite.getUser(), favorite.getProduct());
        if (exists) {
            throw new FavoriteNotUniqueException("Product with id " + favorite.getProduct().getId()
                                                 + " is already favorite for user with id "
                                                 + favorite.getUser().getId());
        }
        return favoriteRepository.save(favorite);
    }

    @Override
    public Favorite getById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(()
                -> new FavoriteNotFoundException("Favorite product with id " + id + " is not found"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }
}
