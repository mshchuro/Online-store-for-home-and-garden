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
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OrderControllerTest {

    @LocalServerPort
    private int port;

    private String userToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // Регистрация и логин пользователя
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"user@example.com\",\"password\":\"password\", \"name\":\"Test User\"}")
                .post("/v1/users/register");
        userToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"user@example.com\",\"password\":\"password\"}")
                .post("/v1/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        // Регистрация и логин администратора
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"admin@example.com\",\"password\":\"admin\", \"name\":\"Admin User\"}")
                .post("/v1/users/register");
        adminToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"admin@example.com\",\"password\":\"admin\"}")
                .post("/v1/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
    @Test
    void testGetAllUserOrders() {
        // Создаем несколько заказов для текущего пользователя
        given()
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.JSON)
                .body("{\"totalPrice\": 50.00, \"orderDate\": \"2025-05-02\"}")
                .post("/v1/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.JSON)
                .body("{\"totalPrice\": 100.00, \"orderDate\": \"2025-05-03\"}")
                .post("/v1/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // Запрашиваем все заказы текущего пользователя
        given()
                .header("Authorization", "Bearer " + userToken)
                .get("/v1/orders")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    void testCreateOrder() {
        given()
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.JSON)
                .body("{\"totalPrice\": 75.50, \"orderDate\": \"2025-05-04\"}")
                .post("/v1/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("totalPrice", equalTo(75.50f));
    }

    @Test
    void testGetOrderStatus() {
        // Сначала создаем заказ, чтобы получить его ID
        int orderId = given()
                .header("Authorization", "Bearer " + userToken)
                .contentType(ContentType.JSON)
                .body("{\"totalPrice\": 25.00, \"orderDate\": \"2025-05-05\"}")
                .post("/v1/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        // Затем пытаемся получить статус этого заказа
        given()
                .header("Authorization", "Bearer " + userToken)
                .get("/v1/orders/" + orderId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(orderId));

        // Попробуем получить заказ другого пользователя (должно быть Forbidden)
        given()
                .header("Authorization", "Bearer " + adminToken)
                .get("/v1/orders/" + orderId)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void testGetOrderStatus_OrderNotFound() {
        given()
                .header("Authorization", "Bearer " + userToken)
                .get("/v1/orders/999") // Запрашиваем несуществующий заказ
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", containsString("Order with id 999 is not found"));
    }
}