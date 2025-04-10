package org.telran.online_store.service;

import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product create(Product product);

    Product getById(Long id);

    Product getByName(String name);

    Product updateProduct(Long id, Product product);

    void delete(Long id);
}
