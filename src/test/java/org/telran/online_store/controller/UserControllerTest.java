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

        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"admin@example.com\",\"password\":\"password\", \"name\":\"Admin\"}")
                .post("/v1/users/register");

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
        String requestBody = """
                {
                  "name": "John Doe",
                  "email": "john@example.com",
                  "phone": "+4915123456789",
                  "password": "secret"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/v1/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("email", equalTo("john@example.com"))
                .body(not(contains("role")))
                .body(not(contains("userRole")));
    }

    @Test
    void testGetAllUsers() {
        // рег. тестового пользователя
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "John Doe",
                          "email": "john@example.com",
                          "phone": "+4915123456789",
                          "password": "secret"
                        }
                        """)
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
        // рег. тестового пользователя и получаем его ID
        int userId = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "Jane Doe",
                          "email": "jane@example.com",
                          "phone": "+4915123456789",
                          "password": "123456"
                        }
                        """)
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
                .body("name", equalTo("Jane Doe"))
                .body("email", equalTo("jane@example.com"));
    }

    @Test
    void testUpdateUser() {
        int userId = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "Update Me",
                          "email": "update@me.com",
                          "phone": "+4915123456789",
                          "password": "oldpass"
                        }
                        """)
                .post("/v1/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "Updated User",
                          "phone": "4915123456789"
                        }
                        """)
                .when()
                .put("/v1/users/" + userId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Updated User"))
                .body("phone", equalTo("4915123456789"));
    }

    @Test
    void testDeleteUser() {
        int userId = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "ToDelete",
                          "email": "delete@me.com",
                          "phone": "+4915123456789",
                          "password": "pass"
                        }
                        """)
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

        // проверка, что пользователь удален
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/v1/users/" + userId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}