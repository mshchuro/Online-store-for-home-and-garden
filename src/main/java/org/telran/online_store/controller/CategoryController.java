package org.telran.online_store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.CategoryConverter;
import org.telran.online_store.dto.CategoryRequestDto;
import org.telran.online_store.dto.CategoryResponseDto;
import org.telran.online_store.entity.Category;
import org.telran.online_store.service.CategoryService;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController implements CategoryApi{

    private final CategoryService categoryService;

    private final CategoryConverter categoryConverter;

    @GetMapping()
    @Override
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        log.info("Get all categories");
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponseDto> dto = categories.stream()
                .map(categoryConverter::toDto)
                .toList();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{categoryId}")
    @Override
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long categoryId) {
        log.info("Get category by id: {}", categoryId);
        Category category = categoryService.getCategoryById(categoryId);
        CategoryResponseDto dto = categoryConverter.toDto(category);
        return ResponseEntity.ok(dto);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        log.info("Creating new category: {}", categoryRequestDto);
        Category category = categoryConverter.toEntity(categoryRequestDto);
        Category createdCategory = categoryService.createCategory(category);
        CategoryResponseDto responseDto = categoryConverter.toDto(createdCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long categoryId) {
        log.info("Deleting category with id: {}", categoryId);
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        log.info("Updating category id: {}, data: {}", categoryId, categoryRequestDto);
        Category category = categoryConverter.toEntity(categoryRequestDto);
        Category updatedCategory = categoryService.updateCategory(categoryId, category);
        CategoryResponseDto responseDto = categoryConverter.toDto(updatedCategory);
        return ResponseEntity.ok(responseDto);
    }
}
