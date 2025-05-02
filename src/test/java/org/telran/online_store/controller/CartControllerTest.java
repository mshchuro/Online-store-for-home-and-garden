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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CartControllerTest {

    @LocalServerPort
    private int port;

    private String token;
    private Long productId1;
    private Long productId2;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Test User\",\"email\":\"user@example.com\",\"password\":\"password\", \"phone\":\"+123456789\"}")
                .post("/v1/users/register");

        token = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"user@example.com\",\"password\":\"password\"}")
                .post("/v1/users/login")
                .then()
                .statusCode(HttpStatus.OK.value()) // Исправлено: ожидаем 200 OK после логина
                .extract()
                .path("token");

        productId1 = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Test Product 1\", \"description\":\"Nice 1\", \"price\":10.0}")
                .post("/v1/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("id");

        productId2 = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Test Product 2\", \"description\":\"Nice 2\", \"price\":20.0}")
                .post("/v1/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("id");
    }

    @Test
    void testAddAndRemoveItemsInCart() {
        // добавление
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"productId\":" + productId1 + "{\"quantity\":1}}")
                .post("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value());

        // добавление
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"productId\":" + productId2 + "{\"quantity\":2}}")
                .post("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value());

        // проверка на 2
        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", equalTo(2));

        // удаление
        given()
                .header("Authorization", "Bearer " + token)
                .delete("/v1/cart/" + productId1)
                .then()
                .statusCode(HttpStatus.OK.value());

        // проверка на 1
        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", equalTo(1))
                .body("items[0].productId", equalTo(productId2.intValue()));
    }

    @Test
    void testDeletedCart() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"productId\":" + productId1 + "{\"quantity\":1}}")
                .post("/v1/cart")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .header("Authorization", "Bearer " + token)
                .delete("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value()) // Исправлено: Ожидаем 200 OK для пустой корзины
                .body("items.size()", equalTo(0));
    }

    @Test
    void testUpdateQuantityIfProductAlreadyInCart() {
        // Add product with quantity 1
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"productId\":" + productId1 + "{\"quantity\":1}}")
                .post("/v1/cart")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Update quantity to 5
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"productId\":" + productId1 + "{\"quantity\":5}}")
                .post("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value()); // Исправлено: Ожидаем 200 OK при обновлении

        // Verify quantity is 5
        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", equalTo(1))
                .body("items[0].quantity", equalTo(5));
    }
}