package org.telran.online_store.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.telran.online_store.dto.FavoriteRequestDto;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.repository.FavoriteJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class FavoriteControllerTest {

    @LocalServerPort
    private int port;

    @Autowired

    private FavoriteJpaRepository favoriteRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private ProductJpaRepository productRepository;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        favoriteRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .email("test@example.com")
                .password("123456")
                .build());

        testProduct = productRepository.save(Product.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .build());
    }

    @Test
    void testCreateFavorite() {
        FavoriteRequestDto request = FavoriteRequestDto.builder()
                .userId(testUser.getId())
                .productId(testProduct.getId())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/favorites")
                .then()
                .statusCode(200)
                .body("userId", equalTo(testUser.getId().intValue()))
                .body("productId", equalTo(testProduct.getId().intValue()));
    }

    @Test
    void testGetAllFavorites() {
        Favorite favorite = Favorite.builder()
                .user(testUser)
                .product(testProduct)
                .build();
        favoriteRepository.save(favorite);

        when()
                .get("/v1/favorites")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void testGetFavoriteById() {
        Favorite favorite = Favorite.builder()
                .user(testUser)
                .product(testProduct)
                .build();
        favorite = favoriteRepository.save(favorite);

        when()
                .get("/v1/favorites/" + favorite.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(favorite.getId().intValue()));
    }

    @Test
    void testDeleteFavorite() {
        Favorite favorite = Favorite.builder()
                .user(testUser)
                .product(testProduct)
                .build();
        favorite = favoriteRepository.save(favorite);

        when()
                .delete("/v1/favorites/" + favorite.getId())
                .then()
                .statusCode(202);

        assertThat(favoriteRepository.findById(favorite.getId())).isEmpty();
    }
}