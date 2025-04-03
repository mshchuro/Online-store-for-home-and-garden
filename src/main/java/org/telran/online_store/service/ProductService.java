package org.telran.online_store.service;

import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product create(Product product);

    Product getById(Long id);

    void delete(Long id);
}
