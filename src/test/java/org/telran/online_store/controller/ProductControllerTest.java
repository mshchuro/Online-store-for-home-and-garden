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
class ProductControllerTest {

    @LocalServerPort
    private int port;

    private String token;
    private int categoryId;

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

        categoryId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Test Category\"}")
                .post("/v1/categories")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");
    }

    @Test
    void testCreateProduct() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Phone\", \"description\": \"Smartphone\", \"price\": 499.99, \"category\": {\"id\": " + categoryId + "} }")
                .post("/v1/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Phone"));
    }

    @Test
    void testGetAllProducts() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Laptop\", \"description\": \"Ultrabook\", \"price\": 999.99, \"category\": {\"id\": " + categoryId + "} }")
                .post("/v1/products")
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .get("/v1/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(1));
    }

//    @Test
//    void testGetAllProductsWithFiltersAndSort() {
//        given()
//                .header("Authorization", "Bearer " + token)
//                .contentType(ContentType.JSON)
//                .post("/v1/products")
//                .then()
//                .statusCode(HttpStatus.OK.value());
//
//        given()
//                .header("Authorization", "Bearer " + token)
//                .contentType(ContentType.JSON)
//                .post("/v1/products")
//                .then()
//                .statusCode(HttpStatus.OK.value());
//
//        given()
//                .queryParam("categoryId", categoryId)
//                .queryParam("minPrice", 100)
//                .queryParam("maxPrice", 1000)
//                .queryParam("sort", "price,desc")
//                .get("/v1/products")
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("size()", greaterThanOrEqualTo(2))
//                .body("[0].price", greaterThanOrEqualTo(199.99f))
//                .body("[1].price", lessThanOrEqualTo(999.99f));
//    }

    @Test
    void testGetProductById() {
        int productId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Tablet\", \"description\": \"Android tablet\", \"price\": 299.99, \"category\": {\"id\": " + categoryId + "} }")
                .post("/v1/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("id");

        given()
                .get("/v1/products/" + productId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Tablet"));
    }

    @Test
    void testUpdateProduct() {
        int productId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Old Name\", \"description\": \"Old Desc\", \"price\": 150.00, \"category\": {\"id\": " + categoryId + "} }")
                .post("/v1/products")
                .then()
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"New Name\", \"description\": \"New Desc\", \"price\": 199.99, \"category\": {\"id\": " + categoryId + "} }")
                .put("/v1/products/" + productId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("New Name"));
    }

    @Test
    void testDeleteProduct() {
        int productId = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"To Be Deleted\", \"description\": \"Temp\", \"price\": 59.99, \"category\": {\"id\": " + categoryId + "} }")
                .post("/v1/products")
                .then()
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + token)
                .delete("/v1/products/" + productId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}