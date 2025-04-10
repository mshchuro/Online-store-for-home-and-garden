package org.telran.online_store.service;

import org.telran.online_store.entity.Category;
import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category createCategory(Category category);

    Category getCategoryById(Long id);

    void deleteCategory(Long id);

    Category updateCategory(Long categoryId, Category category);
}
