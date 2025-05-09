package org.telran.online_store.controller;

import static org.junit.jupiter.api.Assertions.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CartControllerTest {

    @LocalServerPort
    private int port;

    private String token;
    private int categoryId;
    private int productId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        String uniqueEmail = UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        // Регистрация обычного пользователя
        given()
                .contentType(ContentType.JSON)
                .body(String.format("{\"name\":\"Alice Green\", \"email\":\"%s\", \"password\":\"password\", \"phone\":\"123-456-7890\"}", uniqueEmail))
                .post("/v1/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Логин и получение токена для обычного пользователя
        token = given()
                .contentType(ContentType.JSON)
                .body(String.format("{\"email\":\"%s\", \"password\":\"password\"}", uniqueEmail))
                .post("/v1/users/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("token");

        // Получение токена админа
        String adminToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"admin@example.com\", \"password\":\"password\"}")
                .post("/v1/users/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("token");

        // Создание категории и продукта с токеном администратора
        categoryId = given()
                .header("Authorization", "Bearer " + adminToken) // Используем админский токен
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Plants\"}")
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        productId = given()
                .header("Authorization", "Bearer " + adminToken) // Используем админский токен
                .contentType(ContentType.JSON)
                .body(String.format("{ \"name\": \"Rose Bush\", \"description\": \"Beautiful red rose bush for your garden\", \"price\": 15.99, \"category_id\": %d }", categoryId))
                .post("/v1/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("id");
    }

    @Test
    void testAddToCartAndGetCart() {
        // Добавляем товар в корзину
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"product_id\":%d, \"quantity\":2}", productId))
                .post("/v1/cart")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Получаем корзину и проверяем содержимое
        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", is(1))
                .body("items[0].product.id", equalTo(productId))
                .body("items[0].product.name", equalTo("Rose Bush")) // Добавлена проверка названия продукта
                .body("items[0].quantity", equalTo(2));
    }

    @Test
    void testUpdateCartItemQuantity() {
        // Сначала добавим 1 товар
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"product_id\":%d, \"quantity\":1}", productId))
                .post("/v1/cart")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Повторно добавим тот же товар (увеличим количество)
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"product_id\":%d, \"quantity\":3}", productId))
                .post("/v1/cart")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Проверка, что количество увеличилось, а товар не продублировался
        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", is(1))
                .body("items[0].quantity", equalTo(4));
    }

    @Test
    void testDeleteItemFromCart() {
        // Добавим товар
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"product_id\":%d, \"quantity\":1}", productId))
                .post("/v1/cart");

        // Удалим товар
        given()
                .header("Authorization", "Bearer " + token)
                .delete("/v1/cart/" + productId)
                .then()
                .statusCode(HttpStatus.OK.value());

        // Проверим, что корзина пустая
        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", is(0));
    }

    @Test
    void testClearCart() {
        // Добавим товар
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"product_id\":%d, \"quantity\":2}", productId))
                .post("/v1/cart");

        // Очистим корзину
        given()
                .header("Authorization", "Bearer " + token)
                .delete("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value());

        // Проверим, что корзина пустая
        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", is(0));
    }
}