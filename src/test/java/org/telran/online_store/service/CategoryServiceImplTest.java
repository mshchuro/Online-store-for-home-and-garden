package org.telran.online_store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.exception.CategoryNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    private Category cat1;
    private Category cat2;

    @BeforeEach
    void setup() {
        categoryService.getAllCategories().forEach(c -> categoryService.deleteCategory(c.getId()));

        cat1 = new Category();
        cat1.setName("Fertilizer");
        cat1 = categoryService.createCategory(cat1);

        cat2 = new Category();
        cat2.setName("Tools");
        cat2 = categoryService.createCategory(cat2);
    }

    @Test
    void testGetAll() {
        List<Category> categories = categoryService.getAllCategories();
        assertEquals(2, categories.size());
    }

    @Test
    void testGetById() {
        Category category = categoryService.getCategoryById(cat1.getId());
        assertEquals("Fertilizer", category.getName());
    }
    @Test
    void testGetCategoryByIdNotFound() {
        Long nonExistentId = 999L;

        Exception exception = assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(nonExistentId));

        assertTrue(exception.getMessage().contains("Category with id 999 not found"));
    }

    @Test
    void testCreate() {
        Category newCategory = new Category();
        newCategory.setName("New Category");

        Category created = categoryService.createCategory(newCategory);

        assertNotNull(created.getId());
        assertEquals("New Category", created.getName());

        assertEquals(3, categoryService.getAllCategories().size());
    }

    @Test
    void testDeleteById() {
        categoryService.deleteCategory(cat2.getId());
        List<Category> categories = categoryService.getAllCategories();
        assertEquals(1, categories.size());
    }
    @Test
    void testDeleteCategoryNotFound() {
        Long nonExistentId = 999L;
        Exception exception = assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(nonExistentId));

        assertTrue(exception.getMessage().contains("Category with id 999 not found"));
    }

    @Test
    void testDeleteCategoryWithProducts() {
        Category category = new Category();
        category.setName("Electronics");
        category = categoryService.createCategory(category);

        Product product = new Product();
        product.setName("Laptop");
        product.setCategory(category);
        productService.create(product);

        categoryService.deleteCategory(category.getId());

        Product updatedProduct = productService.getById(product.getId());
        assertNull(updatedProduct.getCategory());
    }

    @Test
    void testUpdate() {
        Category update = new Category();
        update.setName("Updated Fertilizer");
        categoryService.updateCategory(cat1.getId(), update);

        Category result = categoryService.getCategoryById(cat1.getId());
        assertEquals("Updated Fertilizer", result.getName());
    }

    @Test
    void testGetByName() {
        Category result = categoryService.getByName("Fertilizer");
        assertNotNull(result);
        assertEquals(cat1.getId(), result.getId());
    }
}