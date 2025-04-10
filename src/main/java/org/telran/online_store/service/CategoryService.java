package org.telran.online_store.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.dto.CategoryUpdateRequest;
import org.telran.online_store.entity.Category;
import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category createCategory(Category category);

    Category getCategoryById(Long id);

    void deleteCategory(Long id);

    Category updateCategory(Long categoryId, CategoryUpdateRequest updateRequest);
}
