package org.telran.online_store.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import org.telran.online_store.AbstractTests;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.exception.CategoryNotFoundException;
import org.telran.online_store.exception.ProductNotFoundException;
import org.telran.online_store.repository.CategoryJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
//@Transactional
public class ProductServiceImplTest extends AbstractTests {

//    @Autowired
//    private ProductServiceImpl productService;
//
//    @Autowired
//    private ProductJpaRepository productRepository;
//
//    @Autowired
//    private CategoryJpaRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        Category category = new Category();
        category.setName("Tools");
        categoryRepo.save(category);
    }

    @Test
    public void testGetAll() {
        Category category1 = new Category();
        category1.setName("Tools");
        category1 = categoryRepo.save(category1);

        Category category2 = new Category();
        category2.setName("Gardening");
        category2 = categoryRepo.save(category2);

        Product product1 = new Product();
        product1.setName("Garden Shovel");
        product1.setPrice(new BigDecimal("25.50"));
        product1.setCategory(category1);
        product1.setDiscountPrice(new BigDecimal("20.00"));
        productRepo.save(product1);

        Product product2 = new Product();
        product2.setName("Lawn Mower");
        product2.setPrice(new BigDecimal("150.00"));
        product2.setCategory(category2);
        product2.setDiscountPrice(new BigDecimal("120.00"));
        productRepo.save(product2);

        Product product3 = new Product();
        product3.setName("Watering Can");
        product3.setPrice(new BigDecimal("15.00"));
        product3.setCategory(category1);
        product3.setDiscountPrice(new BigDecimal("10.00"));
        productRepo.save(product3);

        List<String> sort = List.of("price:asc");

        List<Product> filteredProducts = productService.getAll(category1.getId(), new BigDecimal("10.00"), new BigDecimal("30.00"), true, sort);

        assertNotNull(filteredProducts);
        assertEquals(2, filteredProducts.size());
        Category finalCategory = category1;
        assertTrue(filteredProducts.stream().allMatch(p -> p.getCategory().getId().equals(finalCategory.getId())));
        assertTrue(filteredProducts.stream().allMatch(p -> p.getPrice().compareTo(new BigDecimal("30.00")) <= 0));

        assertTrue(filteredProducts.get(0).getPrice().compareTo(filteredProducts.get(1).getPrice()) <= 0);
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(new Category(1L, "Tools"));

        Product savedProduct = productService.create(product);

        assertNotNull(savedProduct);
        assertEquals("Garden Shovel", savedProduct.getName());
        assertEquals(new BigDecimal("25.50"), savedProduct.getPrice());
    }

    @Test
    public void testGetProductById() {
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepo.save(category);

        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(category);
        product = productRepo.save(product);

        Product foundProduct = productService.getById(product.getId());

        assertNotNull(foundProduct);
        assertEquals("Garden Shovel", foundProduct.getName());
        assertEquals(new BigDecimal("25.50"), foundProduct.getPrice());
    }

    @Test
    public void testGetProductByName() {
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepo.save(category);

        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(category);
        productRepo.save(product);

        Product foundProduct = productService.getByName("Garden Shovel");

        assertNotNull(foundProduct);
        assertEquals("Garden Shovel", foundProduct.getName());
    }

    @Test
    public void testUpdateProduct() {
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepo.save(category);

        Product product = new Product();
        product.setName("Shovel");
        product.setPrice(new BigDecimal("30.00"));
        product.setCategory(category);
        product = productRepo.save(product);

        Product updatedProduct = new Product();
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(new BigDecimal("35.00"));
        updatedProduct.setDiscountPrice(new BigDecimal("30.00"));
        productService.updateProduct(product.getId(), updatedProduct);

        Product foundProduct = productRepo.findById(product.getId()).orElseThrow();
        assertEquals("Updated Description", foundProduct.getDescription());
        assertEquals(new BigDecimal("35.00"), foundProduct.getPrice());
        assertEquals(new BigDecimal("30.00"), foundProduct.getDiscountPrice());
    }

    @Test
    public void testGetAllWithFilters() {
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepo.save(category);

        Product product1 = new Product();
        product1.setName("Garden Shovel");
        product1.setPrice(new BigDecimal("25.50"));
        product1.setDiscountPrice(new BigDecimal("20.00"));
        product1.setCategory(category);
        productRepo.save(product1);

        Product product2 = new Product();
        product2.setName("Lawn Mower");
        product2.setPrice(new BigDecimal("150.00"));
        product2.setDiscountPrice(new BigDecimal("130.00"));
        product2.setCategory(category);
        productRepo.save(product2);

        List<Product> products = productService.getAll(category.getId(), new BigDecimal("10.00"), new BigDecimal("100.00"), null, null);

        assertEquals(1, products.size());
        assertEquals("Garden Shovel", products.get(0).getName());
    }

    @Test
    public void testGetProductByIdNotFound() {
        assertThrows(ProductNotFoundException.class, () -> {
            productService.getById(999L);
        });
    }

    @Test
    public void testGetAllWithSort() {
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepo.save(category);

        Product product1 = new Product();
        product1.setName("Shovel");
        product1.setPrice(new BigDecimal("30.00"));
        product1.setCategory(category);
        productRepo.save(product1);

        Product product2 = new Product();
        product2.setName("Hammer");
        product2.setPrice(new BigDecimal("20.00"));
        product2.setCategory(category);
        productRepo.save(product2);

        List<String> sort = new ArrayList<>();
        sort.add("price:desc");

        List<Product> sortedProducts = productService.getAll(category.getId(), null, null, null, sort);

        assertTrue(sortedProducts.get(0).getPrice().compareTo(sortedProducts.get(1).getPrice()) >= 0);
    }

    @Test
    public void testDeleteProduct() {
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepo.save(category);

        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(category);
        product = productRepo.save(product);

        productService.delete(product.getId());

        assertFalse(productRepo.existsById(product.getId()));
    }

    @Test
    public void testDeleteProductNotFound() {
        assertThrows(ProductNotFoundException.class, () -> {
            productService.delete(999L);
        });
    }

    @Test
    public void testUpdateCategory() {
        Category category1 = new Category();
        category1.setName("Tools");
        category1 = categoryRepo.save(category1);

        Category category2 = new Category();
        category2.setName("Garden");
        category2 = categoryRepo.save(category2);

        Product product = new Product();
        product.setName("Shovel");
        product.setPrice(new BigDecimal("30.00"));
        product.setCategory(category1);
        product = productRepo.save(product);

        productService.updateCategory(product.getId(), category2);

        Product updatedProduct = productRepo.findById(product.getId()).orElseThrow();
        assertEquals(category2.getId(), updatedProduct.getCategory().getId());
    }

    @Test
    public void testGetAllByCategoryId() {
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepo.save(category);

        Product product1 = new Product();
        product1.setName("Shovel");
        product1.setPrice(new BigDecimal("30.00"));
        product1.setCategory(category);
        productRepo.save(product1);

        Product product2 = new Product();
        product2.setName("Hammer");
        product2.setPrice(new BigDecimal("20.00"));
        product2.setCategory(category);
        productRepo.save(product2);

        List<Product> productsByCategory = productService.getAllByCategoryId(category.getId());

        assertEquals(2, productsByCategory.size());
        assertTrue(productsByCategory.stream().anyMatch(p -> p.getName().equals("Shovel")));
        assertTrue(productsByCategory.stream().anyMatch(p -> p.getName().equals("Hammer")));
    }

    @Test
    public void testCreateProductWithCategoryNotFound() {
        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(new Category(999L, "NonExistentCategory"));

        assertThrows(CategoryNotFoundException.class, () -> {
            productService.create(product);
        });
    }

    @Test
    public void testUpdateCategoryProductNotFound() {
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepo.save(category);

        Category finalCategory = category;
        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateCategory(999L, finalCategory);
        });
    }
    @Test
    public void testUpdateProductWithInvalidCategory() {
        Product product = new Product();
        product.setName("Shovel");
        product.setPrice(new BigDecimal("30.00"));

        product = productRepo.save(product);

        Long invalidCategoryId = 999L;

        Product productToUpdate = new Product();
        productToUpdate.setId(product.getId());
        Category category = new Category();
        category.setId(invalidCategoryId);
        productToUpdate.setCategory(category);

        Product finalProduct = product;
        assertThrows(CategoryNotFoundException.class, () -> {
            productService.updateProduct(finalProduct.getId(), productToUpdate);
        });
    }
}