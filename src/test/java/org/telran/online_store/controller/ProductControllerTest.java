package org.telran.online_store.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void testCreateProduct() {
        String requestBody = """
                {
                  "name": "Test Product",
                  "description": "A test product",
                  "price": 99.99
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/v1/products")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Test Product"))
                .body("price", equalTo(99.99f));
    }

    @Test
    void testGetAllProducts() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/v1/products")
                .then()
                .statusCode(200)
                .body("$", is(not(empty())));
    }

    @Test
    void testGetProductById() {
        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "name": "Product for ID Test",
                        "description": "For get by ID",
                        "price": 49.99
                      }
                      """)
                .when()
                .post("/v1/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/v1/products/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Product for ID Test"));
    }

    @Test
    void testUpdateProduct() {
        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "name": "Old Name",
                        "description": "Before update",
                        "price": 10.0
                      }
                      """)
                .post("/v1/products")
                .then()
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "name": "Updated Name",
                        "description": "After update",
                        "price": 20.0
                      }
                      """)
                .when()
                .put("/v1/products/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Name"))
                .body("price", equalTo(20.0f));
    }

    @Test
    void testDeleteProduct() {
        int id = given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "name": "To be deleted",
                        "description": "Will be deleted",
                        "price": 5.0
                      }
                      """)
                .post("/v1/products")
                .then()
                .extract()
                .path("id");

        given()
                .when()
                .delete("/v1/products/" + id)
                .then()
                .statusCode(200);
    }
}