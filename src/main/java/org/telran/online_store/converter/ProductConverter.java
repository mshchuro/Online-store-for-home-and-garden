package org.telran.online_store.converter;

import org.springframework.stereotype.Component;
import org.telran.online_store.dto.ProductDto;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;

import java.util.Optional;

@Component
public class ProductConverter implements Converter<ProductDto, ProductDto, Product> {
    @Override
    public ProductDto toDto(Product product) {
        return ProductDto
                .builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
               // .category(Optional.ofNullable(product.getCategory()).map(Category::getId).orElse(null))
                .image(product.getImageUrl())
                .build();
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        return Product
                .builder()
                .name(productDto.name())
                .description(productDto.description())
                .price(productDto.price())
                //.category(new Category(productDto.category(), null))
                .imageUrl(productDto.image())
                .build();
    }
}
