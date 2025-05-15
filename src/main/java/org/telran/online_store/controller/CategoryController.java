package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.entity.Category;
import org.telran.online_store.handler.GlobalExceptionHandler;
import org.telran.online_store.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Category", description = "API endpoints for product categories")
@SecurityRequirement(name = "bearerAuth")

@RequestMapping("/v1/categories")
public class CategoryController implements CategoryApi{

    private final CategoryService categoryService;

    @GetMapping()
    @Override
    public ResponseEntity<List<Category>> getAll() {
        categoryService.getAllCategories();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{categoryId}")
    @Override
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        Category getCategoryById = categoryService.getCategoryById(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(getCategoryById);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<Category> create(@Valid @RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(categoryId, category);
        return ResponseEntity.ok(updatedCategory);
    }
}
