package org.telran.online_store.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void testRegisterUser() {
        String requestBody = """
                {
                  "name": "John Doe",
                  "email": "john@example.com",
                  "phone": "+4915123456789"
                  "password": "secret",
                  "role": "CLIENT"
                  "userRole": "ADMINISTRATOR"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/v1/users/register")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("email", equalTo("john@example.com"))
                .body("userRole", equalTo("ADMINISTRATOR"));
    }

    @Test
    void testGetAllUsers() {
        String requestBody = """
                {
                  "name": "John Doe",
                  "email": "john@example.com",
                  "phone": "+4915123456789",
                  "password": "secret",
                  "role": "CLIENT"
                  "userRole": "ADMINISTRATOR"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/v1/users/register")
                .then()
                .statusCode(200);

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/v1/users")
                .then()
                .statusCode(200)
                .body("$", is(not(empty())));
    }

    @Test
    void testGetUserById() {
        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "Jane Doe",
                          "email": "jane@example.com",
                          "phone": "+4915123456789",
                          "password": "123456",
                          "userRole": "ADMINISTRATOR"
                        }
                        """)
                .post("/v1/users/register")
                .then()
                .extract()
                .path("id");

        given()
                .when()
                .get("/v1/users/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Jane Doe"))
                .body("email", equalTo("jane@example.com"));
    }

    @Test
    void testUpdateUser() {
        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "Update Me",
                          "email": "update@me.com",
                          "phone": "+4915123456789",
                          "password": "oldpass",
                          "role": "CLIENT",
                          "userRole": "ADMINISTRATOR"
                        }
                        """)
                .post("/v1/users/register")
                .then()
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "Updated User",
                          "phone": "4915123456789"
                        }
                        """)
                .when()
                .put("/v1/users/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated User"))
                .body("phone", equalTo("4915123456789"));
    }

    @Test
    void testDeleteUser() {
        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "ToDelete",
                          "email": "delete@me.com",
                          "phone": "+4915123456789",
                          "password": "pass",
                          "role": "CLIENT",
                          "userRole": "ADMINISTRATOR"
                        }
                        """)
                .post("/v1/users/register")
                .then()
                .extract()
                .path("id");

        given()
                .when()
                .delete("/v1/users/" + id)
                .then()
                .statusCode(200);
    }
}