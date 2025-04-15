package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.exception.CategoryNotFoundException;
import org.telran.online_store.repository.CategoryJpaRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryJpaRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Modifying
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
        categoryRepository.deleteById(id);
    }
}
