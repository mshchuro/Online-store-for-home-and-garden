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
public class UserControllerTest {

    @LocalServerPort
    private int port;

    private String adminToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // Регистрация администратора (если еще не зарегистрирован)
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Admin Joe\",\"email\":\"admin@example.com\",\"phone\":\"5555555555\",\"password\":\"password\"}") // Адаптировано к БД
                .post("/v1/users/register");

        // Получение токена администратора
        adminToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"admin@example.com\",\"password\":\"password\"}")
                .post("/v1/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    void testRegisterUser() {
        String newUserEmail = "john" + System.currentTimeMillis() + "@example.com";
        String newUserName = "John New";
        String newUserPhone = "9876543210";
        String requestBody = String.format("""
                {
                  "name": "%s",
                  "email": "%s",
                  "phone": "%s",
                  "password": "secret"
                }
                """, newUserName, newUserEmail, newUserPhone);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/v1/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("email", equalTo(newUserEmail))
                .body("name", equalTo(newUserName))
                .body("phone", equalTo(newUserPhone))
                .body(not(contains("role")))
                .body(not(contains("userRole")));
    }

    @Test
    void testGetAllUsers() {
        // Сначала регистрируем тестового пользователя с уникальным email
        String testUserEmail = "test" + System.currentTimeMillis() + "@example.com";
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                          "name": "Test User",
                          "email": "%s",
                          "phone": "1122334455",
                          "password": "password"
                        }
                        """, testUserEmail))
                .post("/v1/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .header("Authorization", "Bearer " + adminToken)
                .accept(ContentType.JSON)
                .when()
                .get("/v1/users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", is(not(empty())));
    }

    @Test
    void testGetUserById() {
        // Сначала регистрируем тестового пользователя с уникальным email и получаем его ID
        String testUserEmail = "jane" + System.currentTimeMillis() + "@example.com";
        String testUserName = "Jane New";
        String testUserPhone = "5544332211";
        int userId = given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                          "name": "%s",
                          "email": "%s",
                          "phone": "%s",
                          "password": "123456"
                        }
                        """, testUserName, testUserEmail, testUserPhone))
                .post("/v1/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/v1/users/" + userId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(testUserName))
                .body("email", equalTo(testUserEmail))
                .body("phone", equalTo(testUserPhone));
    }

    @Test
    void testUpdateUser() {
        // Сначала регистрируем пользователя для обновления с уникальным email
        String testUserEmail = "update" + System.currentTimeMillis() + "@example.com";
        int userId = given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                          "name": "Update Me",
                          "email": "%s",
                          "phone": "0123456789",
                          "password": "oldpass"
                        }
                        """, testUserEmail))
                .post("/v1/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        String updatedUserName = "Updated Person";
        String updatedUserPhone = "9876501234";
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                          "name": "%s",
                          "phone": "%s"
                        }
                        """, updatedUserName, updatedUserPhone))
                .when()
                .put("/v1/users/" + userId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(updatedUserName))
                .body("phone", equalTo(updatedUserPhone));
    }

    @Test
    void testDeleteUser() {
        // Сначала регистрируем пользователя для удаления с уникальным email
        String testUserEmail = "delete" + System.currentTimeMillis() + "@example.com";
        String testUserName = "ToDelete User";
        String testUserPhone = "1199228833";
        int userId = given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                          "name": "%s",
                          "email": "%s",
                          "phone": "%s",
                          "password": "pass"
                        }
                        """, testUserName, testUserEmail, testUserPhone))
                .post("/v1/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .delete("/v1/users/" + userId)
                .then()
                .statusCode(HttpStatus.OK.value());

        // Проверяем, что пользователь действительно удален
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/v1/users/" + userId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}