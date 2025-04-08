package org.telran.online_store.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.telran.online_store.entity.Product;
import org.telran.online_store.repository.ProductJpaRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
//@Sql("/dataInit.sql")
@Transactional
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @BeforeTransaction
    private void initDatabase() {
        productJpaRepository.save(new Product("Lily", "Flowers", new BigDecimal(22.08)));
    }

    @Test
//    @Rollback(true)
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
        assertEquals("Lily", productService.getById(1L).getName());
    }

    @Test
//    @Rollback(true)
    void testDeleteProductById() {
        productService.delete(2L);
        assertEquals(1, productService.getAll().size());
    }
}