package org.telran.online_store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.User;
import org.telran.online_store.exception.*;
import org.telran.online_store.repository.FavoriteJpaRepository;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteJpaRepository favoriteRepository;

    private final UserService userService;

    @Override
    public List<Favorite> getAll() {
        User currentUser = userService.getCurrentUser();
        return favoriteRepository.findAllByUser(currentUser); //getAllByUser
    }

    @Override
    @Modifying
    @Transactional
    public Favorite create(Favorite favorite) {
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
