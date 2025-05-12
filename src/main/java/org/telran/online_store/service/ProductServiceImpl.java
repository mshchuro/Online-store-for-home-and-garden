package org.telran.online_store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.exception.CategoryNotFoundException;
import org.telran.online_store.exception.ProductNotFoundException;
import org.telran.online_store.repository.CategoryJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;
import org.telran.online_store.specification.ProductSpecification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductJpaRepository productRepository;

    private final CategoryJpaRepository categoryRepository;

    public List<Product> getAll(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Boolean discount, List<String> sort) {

        Specification<Product> spec = ProductSpecification.filterBy(categoryId, minPrice, maxPrice, discount);

        Sort sortObj;
        if (sort != null && !sort.isEmpty()) {
            List<Sort.Order> orders = new ArrayList<>();

            for (String sortParam : sort) {

                String[] parts = sortParam.split(":");
                if (parts.length == 2) {
                    String field = parts[0];
                    String direction = parts[1];

                    Sort.Order order = "desc".equalsIgnoreCase(direction)
                            ? Sort.Order.desc(field)
                            : Sort.Order.asc(field);

                    if ("discountPrice".equals(field)) {
                        order = order.nullsLast();
                    }

                    orders.add(order);
                }
            }

            sortObj = orders.isEmpty() ? Sort.by("name") : Sort.by(orders);

        } else {
            sortObj = Sort.by("name");
        }
        return productRepository.findAll(spec, sortObj);
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

        if (product.getDiscountPrice() != null) {
            existingProduct.setDiscountPrice(product.getDiscountPrice());
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

    @Override
    public List<Product> getAllByCategoryId(Long id) {
        return productRepository.findAllByCategory_Id(id);
    }

    @Override
    public void updateCategory(Long id, Category category) {
        Product product = getById(id);
        product.setCategory(category);
        productRepository.save(product);
    }
}
