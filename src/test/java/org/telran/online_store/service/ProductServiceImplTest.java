package org.telran.online_store.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.telran.online_store.entity.Product;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(value = "/prodDataInit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Test
    void testCreateProduct() {
        String productName = "Flowers";
        BigDecimal productPrice = new BigDecimal("22.99");

        Product product = new Product();
        product.setName(productName);
        product.setPrice(productPrice);
        product.setDiscountPrice(new BigDecimal("0.99"));

        productService.create(product);

        Product fromDb = productService.getByName(productName);

        assertNotNull(fromDb);
        assertEquals(productName, fromDb.getName());
        assertEquals(productPrice, fromDb.getPrice());
    }

    @Test
    void testGetAllProducts() {
        assertEquals(2, productService.getAll().size());
    }

    @Test
    void testGetProductById() {
        Product product = productService.getByName("Lily");
        assertNotNull(product);
        assertEquals("Lily", productService.getById(product.getId()).getName());
    }

    @Test
    void testUpdateProduct(){
        Product product = productService.getByName("Lily");

    }

    @Test
    void testDeleteProductById() {
        List<Product> products = productService.getAll();
        assertEquals(2, products.size());
        productService.delete(products.get(0).getId());
        assertEquals(1, productService.getAll().size());
    }
}