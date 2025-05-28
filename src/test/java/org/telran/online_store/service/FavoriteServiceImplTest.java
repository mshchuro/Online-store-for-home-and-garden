package org.telran.online_store.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.telran.online_store.AbstractServicesTests;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.exception.FavoriteNotUniqueException;
import org.telran.online_store.exception.FavoriteNotFoundException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class FavoriteServiceImplTest extends AbstractServicesTests {

    @Test
    void testCreateFavorite() {
        Favorite favorite = new Favorite();
        favorite.setProduct(testProduct);

        Favorite saved = favoriteService.create(favorite);

        assertNotNull(saved.getId());
        assertEquals(testProduct.getId(), saved.getProduct().getId());
        assertEquals(testUser.getId(), saved.getUser().getId());
    }

    @Test
    void testGetAllFavorites() {
        Favorite favorite = new Favorite();
        favorite.setProduct(testProduct);
        favoriteService.create(favorite);

        List<Favorite> favorites = favoriteService.getAll();
        assertEquals(1, favorites.size());
    }

    @Test
    void testDeleteFavorite() {
        Favorite favorite = new Favorite();
        favorite.setProduct(testProduct);
        Favorite saved = favoriteService.create(favorite);

        favoriteService.delete(saved.getId());

        List<Favorite> remaining = favoriteService.getAll();
        assertEquals(0, remaining.size());
    }

    @Test
    void testDuplicateFavoriteThrowsException() {
        Favorite favorite = new Favorite();
        favorite.setProduct(testProduct);
        favoriteService.create(favorite);

        Favorite duplicate = new Favorite();
        duplicate.setProduct(testProduct);

        assertThrows(FavoriteNotUniqueException.class, () -> favoriteService.create(duplicate));
    }

    @Test
    void testDeleteNonExistingFavoriteThrowsException() {
        Long nonExistingId = 999L;

        assertThrows(FavoriteNotFoundException.class, () -> favoriteService.delete(nonExistingId));
    }
}