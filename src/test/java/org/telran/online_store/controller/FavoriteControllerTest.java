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
import org.telran.online_store.enums.UserRole;
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
    private User testUser;
    private String token;
    private Long userId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        favoriteRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .name("Alice Green")
                .email("alice@example.com")
                .phone("1234567890")
                .password("$2a$10$nhJq7EkEQUuoOM1fBQ4vJ.kEXAh9RGZl30lSUlcValMMJ1g9wVT6u")
                .role(UserRole.CLIENT)
                .build());
        userId = testUser.getId();

        testProduct = productRepository.save(Product.builder()
                .name("Shovel")
                .description("Durable steel shovel with wooden handle")
                .price(BigDecimal.valueOf(25.50))
                .imageUrl("images/shovel.jpg")
                .build());

        token = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"alice@example.com\",\"password\":\"password\"}") // Используем пароль из тестов, а не хеш из БД
                .post("/v1/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
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

        // Проверяем, что запись появилась в базе данных
        assertThat(favoriteRepository.existsByUserAndProduct(testUser, testProduct)).isTrue();
    }

    @Test
    void testGetAllFavorites() {
        // Добавляем запись в избранное
        favoriteRepository.save(Favorite.builder()
                .user(testUser)
                .product(testProduct)
                .build());

        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/favorites")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].userId", equalTo(userId.intValue()))
                .body("[0].productId", equalTo(testProduct.getId().intValue()));
    }

    @Test
    void testDeleteFavorite() {
        // Создаем запись в избранном
        Favorite favorite = favoriteRepository.save(Favorite.builder()
                .user(testUser)
                .product(testProduct)
                .build());

        given()
                .header("Authorization", "Bearer " + token)
                .delete("/v1/favorites/" + favorite.getId())
                .then()
                .statusCode(200);

        // Проверяем, что запись удалена из базы данных
        assertThat(favoriteRepository.findById(favorite.getId())).isEmpty();
    }
}