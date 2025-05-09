package org.telran.online_store.controller;

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
class CategoryControllerTest {

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // Регистрация администратора (если еще не зарегистрирован)
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Admin Joe\",\"email\":\"admin@example.com\",\"phone\":\"5555555555\",\"password\":\"password\"}") // Адаптировано к БД Users
                .post("/v1/users/register");

        // Получение токена администратора
        token = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"admin@example.com\",\"password\":\"password\"}")
                .post("/v1/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    void testGetAllCategories() {
        // Создаем категории, соответствующие данным в базе
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Tools\"}") // Название из Categories
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Soil\"}") // Название из Categories
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .get("/v1/categories")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(2))
                .body("[0].name", notNullValue()) // Проверяем, что есть как минимум две категории и у них есть имена
                .body("[1].name", notNullValue());
    }

    @Test
    void testCreateCategory() {
        String newCategoryName = "Decor"; // Новое название, соответствующее структуре базы
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"name\":\"%s\"}", newCategoryName))
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo(newCategoryName));
    }

    @Test
    void testGetCategoryById() {
        String categoryNameToGet = "Seeds"; // Название из Categories
        int categoryId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"name\":\"%s\"}", categoryNameToGet))
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .get("/v1/categories/" + categoryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(categoryId))
                .body("name", equalTo(categoryNameToGet));
    }

    @Test
    void testUpdateCategory() {
        String oldCategoryName = "Old Items";
        String updatedCategoryName = "New Items";
        int categoryId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"name\":\"%s\"}", oldCategoryName))
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"name\":\"%s\"}", updatedCategoryName))
                .put("/v1/categories/" + categoryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(categoryId))
                .body("name", equalTo(updatedCategoryName));
    }

    @Test
    void testDeleteCategory() {
        String categoryToDeleteName = "Temporary";
        int categoryId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format("{\"name\":\"%s\"}", categoryToDeleteName))
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + token)
                .delete("/v1/categories/" + categoryId)
                .then()
                .statusCode(HttpStatus.OK.value());

        // Проверяем, что категория действительно удалена (опционально)
        given()
                .header("Authorization", "Bearer " + token)
                .get("/v1/categories/" + categoryId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}