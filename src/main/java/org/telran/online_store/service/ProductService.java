package org.telran.online_store.service;

import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<Product> getAll(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Boolean discount, List<String> sort);

    Product create(Product product);

    Product getById(Long id);

    Product getByName(String name);

    Product updateProduct(Long id, Product product);

    void delete(Long id);

    List<Product> getAllByCategoryId(Long id);

    void updateCategory(Long id, Category category);

}
