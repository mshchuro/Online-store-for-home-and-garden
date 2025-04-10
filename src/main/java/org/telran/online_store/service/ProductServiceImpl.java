package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.exception.CategoryNotFoundException;
import org.telran.online_store.exception.ProductNotFoundException;
import org.telran.online_store.repository.CategoryJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductJpaRepository productRepository;

    @Autowired
    CategoryJpaRepository categoryRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @Modifying
    @Transactional
    public Product create(Product product) {
        Long categoryId = Optional.ofNullable(product.getCategory()).map(Category::getId).orElse(null);

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new CategoryNotFoundException("Category with id " + categoryId + " is not found"));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(()
                -> new ProductNotFoundException("Product with id " + id + " is not found"));
    }

    @Override
    public Product getByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    @Modifying
    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " is not found"));

        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }

        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }

        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }

        if (product.getImageUrl() != null) {
            existingProduct.setImageUrl(product.getImageUrl());
        }

        Long categoryId = Optional.ofNullable(product.getCategory()).map(Category::getId).orElse(null);

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new CategoryNotFoundException("Category with id " + categoryId + " is not found"));
            product.setCategory(category);
            existingProduct.setCategory(category);
        }

        return productRepository.save(existingProduct);
    }

    @Override
    @Modifying
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }
}
