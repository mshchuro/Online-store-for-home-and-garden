package org.telran.online_store.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles("test")
    class CategoryControllerTest {

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"admin@example.com\",\"password\":\"password\"}")
                .post("/v1/users/register");

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

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Category One\"}")
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Category Two\"}")
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .get("/v1/categories")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(2));
    }



    @Test
    void testCreateCategory() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Test Category\"}")
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void testGetCategoryById() {
        int categoryId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Another Category\"}")
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .get("/v1/categories/" + categoryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Another Category"));
    }

    @Test
    void testUpdateCategory() {
        int categoryId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Old Category\"}")
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Updated Category\"}")
                .put("/v1/categories/" + categoryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Updated Category"));
    }

    @Test
    void testDeleteCategory() {
        int categoryId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Category to Delete\"}")
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
    }
    }