package org.telran.online_store.converter;

import org.springframework.stereotype.Component;
import org.telran.online_store.dto.ProductRequestDto;
import org.telran.online_store.dto.ProductResponseDto;
import org.telran.online_store.entity.Product;

@Component
public class ProductConverter implements Converter<ProductRequestDto, ProductResponseDto, Product> {

    @Override
    public ProductResponseDto toDto(Product product) {
        return ProductResponseDto
                .builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                // .category(Optional.ofNullable(product.getCategory()).map(Category::getId).orElse(null))
                .image(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    @Override
    public Product toEntity(ProductRequestDto productDto) {
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
