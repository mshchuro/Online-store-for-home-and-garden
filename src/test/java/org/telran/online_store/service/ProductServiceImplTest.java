package org.telran.online_store.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.repository.CategoryJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductJpaRepository productRepository;

    @Autowired
    private CategoryJpaRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        // Создаем тестовую категорию перед каждым тестом
        Category category = new Category();
        category.setName("Tools");
        categoryRepository.save(category);
    }

    @Test
    public void testCreateProduct() {
        // Создаем новый продукт
        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(new Category(1L, "Tools"));

        // Сохраняем продукт
        Product savedProduct = productService.create(product);

        // Проверяем, что продукт сохранен
        assertNotNull(savedProduct);
        assertEquals("Garden Shovel", savedProduct.getName());
        assertEquals(new BigDecimal("25.50"), savedProduct.getPrice());
    }

    @Test
    public void testGetProductById() {
        // Сначала создаем продукт для теста
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(category);
        product = productRepository.save(product);

        // Теперь получаем продукт по id
        Product foundProduct = productService.getById(product.getId());

        // Проверяем, что продукт найден и имеет правильные данные
        assertNotNull(foundProduct);
        assertEquals("Garden Shovel", foundProduct.getName());
        assertEquals(new BigDecimal("25.50"), foundProduct.getPrice());
    }

    @Test
    public void testGetProductByName() {
        // Создаем продукт для теста
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(category);
        productRepository.save(product);

        // Проверяем, что продукт найден по имени
        Product foundProduct = productService.getByName("Garden Shovel");

        assertNotNull(foundProduct);
        assertEquals("Garden Shovel", foundProduct.getName());
    }

    @Test
    public void testDeleteProduct() {
        // Создаем продукт для теста
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(category);
        product = productRepository.save(product);

        // Удаляем продукт
        productService.delete(product.getId());

        // Проверяем, что продукт был удален
        assertFalse(productRepository.existsById(product.getId()));
    }
}