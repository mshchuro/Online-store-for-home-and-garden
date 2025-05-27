package org.telran.online_store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.exception.CategoryNotFoundException;
import org.telran.online_store.repository.CategoryJpaRepository;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryJpaRepository categoryRepository;

    private final ProductService productService;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new CategoryNotFoundException("Category with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        List<Product> products = productService.getAllByCategoryId(id);
        products.forEach(product -> productService.updateCategory(product.getId(), null));
        Category category = getCategoryById(id);
        categoryRepository.deleteById(category.getId());
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, Category category) {
        Category entity = getCategoryById(id);
        entity.setName(category.getName());
        return categoryRepository.save(entity);

    }

    @Override
    public Category getByName(String name) {
        return categoryRepository.findByName(name);
    }
}
