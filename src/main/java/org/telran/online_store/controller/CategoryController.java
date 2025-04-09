package org.telran.online_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.entity.Category;
import org.telran.online_store.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.accepted().build();
    }
}
