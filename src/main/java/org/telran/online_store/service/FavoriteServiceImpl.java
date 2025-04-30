package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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
    private UserService userService;

    @Override
    public List<Favorite> getAll() {
        User currentUser = userService.getCurrentUser();
        return favoriteRepository.findAllByUser(currentUser); //getAllByUser
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
        favorite.setUser(userService.getCurrentUser());
        boolean exists = favoriteRepository.existsByUserAndProduct(favorite.getUser(), favorite.getProduct());
        if (exists) {
            throw new FavoriteNotUniqueException("Product with id " + favorite.getProduct().getId()
                                                 + " is already favorite for user with id "
                                                 + favorite.getUser().getId());
        }
        return favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(()
                -> new FavoriteNotFoundException("Favorite with id " + id + " for current user is not found :("));
        User currentUser = userService.getCurrentUser();
        User user = favorite.getUser();

        if (!currentUser.equals(user)) {
            throw new AccessDeniedException("You are not allowed to delete this favorite.");
        }

        favoriteRepository.deleteById(id);
    }
}
