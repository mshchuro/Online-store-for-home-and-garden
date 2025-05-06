package org.telran.online_store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.telran.online_store.entity.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

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