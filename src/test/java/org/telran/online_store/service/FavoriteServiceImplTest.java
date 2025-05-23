package org.telran.online_store.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.telran.online_store.AbstractTests;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.exception.FavoriteNotUniqueException;
import org.telran.online_store.exception.FavoriteNotFoundException;
import org.telran.online_store.repository.FavoriteJpaRepository;
import org.telran.online_store.repository.OrderJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@ActiveProfiles("test")
class FavoriteServiceImplTest extends AbstractTests {
//
//    @Autowired
//    private FavoriteService favoriteService;
//
//    @Autowired
//    private FavoriteJpaRepository favoriteRepo;
//
//    @Autowired
//    private OrderJpaRepository orderRepo;
//
//    @Autowired
//    private UserJpaRepository userRepo;
//
//    @Autowired
//    private ProductJpaRepository productRepo;
//
//    private User testUser;
//    private Product testProduct;
//
//    @BeforeEach
//    void setUp() {
//        orderRepo.deleteAll();
//        favoriteRepo.deleteAll();
//        productRepo.deleteAll();
//        userRepo.deleteAll();
//
//        testUser = User.builder()
//                .name("Test User")
//                .email("test@example.com")
//                .phone("1234567890")
//                .password("password")
//                .role(UserRole.CLIENT)
//                .build();
//        testUser = userRepo.save(testUser);
//
//        testProduct = Product.builder()
//                .name("Test Product")
//                .description("Description")
//                .price(BigDecimal.valueOf(100))
//                .imageUrl("image.jpg")
//                .build();
//        testProduct = productRepo.save(testProduct);
//
//        UsernamePasswordAuthenticationToken auth =
//                new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword(), List.of());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }

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