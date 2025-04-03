package org.telran.online_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.online_store.entity.Product;
import org.telran.online_store.repository.ProductJpaRepository;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductJpaRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
