package org.telran.online_store.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.telran.online_store.entity.Category;
import org.telran.online_store.repository.CategoryJpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceImplTest {

    @Autowired
    private CategoryJpaRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;


    @Test
    public void testGetAll() {
        List<Category> categories = categoryService.getAllCategories();
        assertEquals(2, categories.size());
    }
    @Test
    public void testGetById() {
        Category category = categoryService.getCategoryById(1L);
        assertEquals("Category 1", category.getName());
    }

    @Test
    public void testCreate() {
        Category newCategory = new Category(null, "New Category");
        Category createdCategory = categoryService.createCategory(newCategory);
        assertNotNull(createdCategory.getId());
        assertEquals("New Category", createdCategory.getName());


        assertEquals(3, categoryService.getAllCategories().size());
    }

    @Test
    public void testDeleteById() {
        categoryService.deleteCategory(2L);
        assertEquals(1, categoryService.getAllCategories().size());
    }
}