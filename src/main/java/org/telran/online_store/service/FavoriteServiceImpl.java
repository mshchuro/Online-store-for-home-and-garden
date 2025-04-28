package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.User;
import org.telran.online_store.exception.*;
import org.telran.online_store.repository.FavoriteJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteJpaRepository favoriteRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private ProductJpaRepository productRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Favorite> getAllByUser(User user) {
        return favoriteRepository.findAllByUser(user); //getAllByUser
    }

    @Override
    @Modifying
    @Transactional
    public Favorite create(Favorite favorite) {
//        Optional.ofNullable(favorite.getUser()).map(User::getId).ifPresent(id -> {
//            User user = userService.getById(id);
//            favorite.setUser(user);
//        });
//
//        Optional.ofNullable(favorite.getProduct()).map(Product::getId).ifPresent(id -> {
//            Product product = productRepository.findById(id).orElseThrow(
//                    () -> new ProductNotFoundException("Product with id " + id + " is not found"));
//            favorite.setProduct(product);
//        });
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
