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
import org.telran.online_store.repository.FavoriteJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.repository.UserJpaRepository;
import java.math.BigDecimal;
import static io.restassured.RestAssured.given;
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

    private Product testProduct;
    private String token;
    private Long userId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        favoriteRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();

        testProduct = productRepository.save(Product.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .build());

        // Регистрация пользователя через API
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"test@example.com\",\"password\":\"123456\", \"name\":\"Lily\", \"phone\":\"1234567890123\"}")
                .post("/v1/users/register")
                .then()
                .statusCode(201);

        // Авторизация для получения токена
        token = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"test@example.com\",\"password\":\"123456\"}")
                .post("/v1/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        // Получаем userId для сравнения
        userId = userRepository.findByEmail("test@example.com").getId();
    }

    @Test
    void testCreateFavorite() {
        FavoriteRequestDto request = FavoriteRequestDto.builder()
                .productId(testProduct.getId())
                .build();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/favorites")
                .then()
                .statusCode(200)
                .body("userId", equalTo(userId.intValue()))
                .body("productId", equalTo(testProduct.getId().intValue()));
    }

    @Test
    void testGetAllFavorites() {
        favoriteRepository.save(Favorite.builder()
                .user(userRepository.findById(userId).orElseThrow())
                .product(testProduct)
                .build());

        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/favorites")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }


    @Test
    void testDeleteFavorite() {
        Favorite favorite = favoriteRepository.save(Favorite.builder()
                .user(userRepository.findById(userId).orElseThrow())
                .product(testProduct)
                .build());

        given()
                .header("Authorization", "Bearer " + token)
                .delete("/v1/favorites/" + favorite.getId())
                .then()
                .statusCode(200);

        assertThat(favoriteRepository.findById(favorite.getId())).isEmpty();
    }
}