package org.telran.online_store.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.exception.CategoryNotFoundException;
import org.telran.online_store.exception.ProductNotFoundException;
import org.telran.online_store.repository.CategoryJpaRepository;
import org.telran.online_store.repository.ProductJpaRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductJpaRepository productRepository;

    @Autowired
    private CategoryJpaRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        // Создаем тестовую категорию перед каждым тестом
        Category category = new Category();
        category.setName("Tools");
        categoryRepository.save(category);
    }

    @Test
    public void testGetAll() {
        // Создаем категории для теста
        Category category1 = new Category();
        category1.setName("Tools");
        category1 = categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Gardening");
        category2 = categoryRepository.save(category2);

        // Создаем продукты для теста
        Product product1 = new Product();
        product1.setName("Garden Shovel");
        product1.setPrice(new BigDecimal("25.50"));
        product1.setCategory(category1);
        product1.setDiscountPrice(new BigDecimal("20.00"));
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Lawn Mower");
        product2.setPrice(new BigDecimal("150.00"));
        product2.setCategory(category2);
        product2.setDiscountPrice(new BigDecimal("120.00"));
        productRepository.save(product2);

        Product product3 = new Product();
        product3.setName("Watering Can");
        product3.setPrice(new BigDecimal("15.00"));
        product3.setCategory(category1);
        product3.setDiscountPrice(new BigDecimal("10.00"));
        productRepository.save(product3);

        // Параметры фильтрации
        List<String> sort = List.of("price:asc");  // сортировка по цене

        // Фильтрация по категории и минимальной цене
        List<Product> filteredProducts = productService.getAll(category1.getId(), new BigDecimal("10.00"), new BigDecimal("30.00"), true, sort);

        // Проверяем, что отфильтрированные продукты соответствуют ожиданиям
        assertNotNull(filteredProducts);
        assertEquals(2, filteredProducts.size());
        Category finalCategory = category1;
        assertTrue(filteredProducts.stream().allMatch(p -> p.getCategory().getId().equals(finalCategory.getId())));
        assertTrue(filteredProducts.stream().allMatch(p -> p.getPrice().compareTo(new BigDecimal("30.00")) <= 0));

        // Проверяем, что сортировка по цене работает правильно
        assertTrue(filteredProducts.get(0).getPrice().compareTo(filteredProducts.get(1).getPrice()) <= 0);
    }

    @Test
    public void testCreateProduct() {
        // Создаем новый продукт
        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(new Category(1L, "Tools"));

        // Сохраняем продукт
        Product savedProduct = productService.create(product);

        // Проверяем, что продукт сохранен
        assertNotNull(savedProduct);
        assertEquals("Garden Shovel", savedProduct.getName());
        assertEquals(new BigDecimal("25.50"), savedProduct.getPrice());
    }

    @Test
    public void testGetProductById() {
        // Сначала создаем продукт для теста
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(category);
        product = productRepository.save(product);

        // Теперь получаем продукт по id
        Product foundProduct = productService.getById(product.getId());

        // Проверяем, что продукт найден и имеет правильные данные
        assertNotNull(foundProduct);
        assertEquals("Garden Shovel", foundProduct.getName());
        assertEquals(new BigDecimal("25.50"), foundProduct.getPrice());
    }

    @Test
    public void testGetProductByName() {
        // Создаем продукт для теста
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(category);
        productRepository.save(product);

        // Проверяем, что продукт найден по имени
        Product foundProduct = productService.getByName("Garden Shovel");

        assertNotNull(foundProduct);
        assertEquals("Garden Shovel", foundProduct.getName());
    }

    @Test
    public void testUpdateProduct() {
        // Создаем категорию и продукт для обновления
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Shovel");
        product.setPrice(new BigDecimal("30.00"));
        product.setCategory(category);
        product = productRepository.save(product);

        // Обновляем продукт
        Product updatedProduct = new Product();
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(new BigDecimal("35.00"));
        updatedProduct.setDiscountPrice(new BigDecimal("30.00"));
        productService.updateProduct(product.getId(), updatedProduct);

        // Проверяем, что продукт обновился
        Product foundProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals("Updated Description", foundProduct.getDescription());
        assertEquals(new BigDecimal("35.00"), foundProduct.getPrice());
        assertEquals(new BigDecimal("30.00"), foundProduct.getDiscountPrice());
    }

    @Test
    public void testGetAllWithFilters() {
        // Создаем категорию и несколько продуктов
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product1 = new Product();
        product1.setName("Garden Shovel");
        product1.setPrice(new BigDecimal("25.50"));
        product1.setDiscountPrice(new BigDecimal("20.00"));
        product1.setCategory(category);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Lawn Mower");
        product2.setPrice(new BigDecimal("150.00"));
        product2.setDiscountPrice(new BigDecimal("130.00"));
        product2.setCategory(category);
        productRepository.save(product2);

        // Запрашиваем все продукты с фильтром по цене
        List<Product> products = productService.getAll(category.getId(), new BigDecimal("10.00"), new BigDecimal("100.00"), null, null);

        // Проверяем, что список содержит только один продукт
        assertEquals(1, products.size());
        assertEquals("Garden Shovel", products.get(0).getName());
    }

    @Test
    public void testGetProductByIdNotFound() {
        // Пытаемся получить продукт, который не существует
        assertThrows(ProductNotFoundException.class, () -> {
            productService.getById(999L); // Продукта с таким ID нет
        });
    }

    @Test
    public void testGetAllWithSort() {
        // Создаём несколько продуктов с разными ценами
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product1 = new Product();
        product1.setName("Shovel");
        product1.setPrice(new BigDecimal("30.00"));
        product1.setCategory(category);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Hammer");
        product2.setPrice(new BigDecimal("20.00"));
        product2.setCategory(category);
        productRepository.save(product2);

        List<String> sort = new ArrayList<>();
        sort.add("price:desc"); // тестируем сортировку по цене по убыванию

        List<Product> sortedProducts = productService.getAll(category.getId(), null, null, null, sort);

        assertTrue(sortedProducts.get(0).getPrice().compareTo(sortedProducts.get(1).getPrice()) >= 0);
    }

    @Test
    public void testDeleteProduct() {
        // Создаем продукт для теста
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(category);
        product = productRepository.save(product);

        // Удаляем продукт
        productService.delete(product.getId());

        // Проверяем, что продукт был удален
        assertFalse(productRepository.existsById(product.getId()));
    }

    @Test
    public void testDeleteProductNotFound() {
        // Пытаемся удалить продукт, которого нет в базе
        assertThrows(ProductNotFoundException.class, () -> {
            productService.delete(999L); // Продукта с таким ID нет
        });
    }

    @Test
    public void testUpdateCategory() {
        // Создаём категорию и продукт
        Category category1 = new Category();
        category1.setName("Tools");
        category1 = categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Garden");
        category2 = categoryRepository.save(category2);

        Product product = new Product();
        product.setName("Shovel");
        product.setPrice(new BigDecimal("30.00"));
        product.setCategory(category1);
        product = productRepository.save(product);

        // Обновляем категорию продукта
        productService.updateCategory(product.getId(), category2);

        // Проверяем, что категория обновилась
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(category2.getId(), updatedProduct.getCategory().getId());
    }

    @Test
    public void testGetAllByCategoryId() {
        // Создаём категорию и несколько продуктов
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        Product product1 = new Product();
        product1.setName("Shovel");
        product1.setPrice(new BigDecimal("30.00"));
        product1.setCategory(category);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Hammer");
        product2.setPrice(new BigDecimal("20.00"));
        product2.setCategory(category);
        productRepository.save(product2);

        // Получаем продукты по категории
        List<Product> productsByCategory = productService.getAllByCategoryId(category.getId());

        // Проверяем, что оба продукта в списке
        assertEquals(2, productsByCategory.size());
        assertTrue(productsByCategory.stream().anyMatch(p -> p.getName().equals("Shovel")));
        assertTrue(productsByCategory.stream().anyMatch(p -> p.getName().equals("Hammer")));
    }

    @Test
    public void testCreateProductWithCategoryNotFound() {
        // Создаем продукт с несуществующей категорией
        Product product = new Product();
        product.setName("Garden Shovel");
        product.setPrice(new BigDecimal("25.50"));
        product.setCategory(new Category(999L, "NonExistentCategory"));  // Категория с id 999 не существует

        // Пытаемся сохранить продукт и проверяем, что выбрасывается исключение
        assertThrows(CategoryNotFoundException.class, () -> {
            productService.create(product);
        });
    }

    @Test
    public void testUpdateCategoryProductNotFound() {
        // Создаем категорию
        Category category = new Category();
        category.setName("Tools");
        category = categoryRepository.save(category);

        // Пытаемся обновить категорию у несуществующего продукта
        Category finalCategory = category;
        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateCategory(999L, finalCategory);  // ID 999 не существует
        });
    }
    @Test
    public void testUpdateProductWithInvalidCategory() {
        // Создаем продукт, но не добавляем к нему категорию
        Product product = new Product();
        product.setName("Shovel");
        product.setPrice(new BigDecimal("30.00"));

        // Сохраняем продукт в репозитории
        product = productRepository.save(product);

        // Создаём ID, которого нет в базе данных
        Long invalidCategoryId = 999L;  // Неверный ID, который точно не существует

        // Создаем новый продукт с данным ID категории
        Product productToUpdate = new Product();
        productToUpdate.setId(product.getId());
        Category category = new Category();
        category.setId(invalidCategoryId);  // Устанавливаем неверный ID категории
        productToUpdate.setCategory(category);

        // Проверяем, что выбрасывается исключение CategoryNotFoundException
        Product finalProduct = product;
        assertThrows(CategoryNotFoundException.class, () -> {
            productService.updateProduct(finalProduct.getId(), productToUpdate);
        });
    }
}