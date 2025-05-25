package org.telran.online_store.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telran.online_store.dto.ProductRequestDto;
import org.telran.online_store.dto.ProductResponseDto;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.CategoryService;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProductConverter implements Converter<ProductRequestDto, ProductResponseDto, Product> {

    private final CategoryService categoryService;

    @Override
    public ProductResponseDto toDto(Product product) {
        return ProductResponseDto
                .builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .categoryId(Optional.ofNullable(product.getCategory()).map(Category::getId).orElse(null))
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
                .discountPrice(productDto.discountPrice())
                .category(
                        productDto.categoryId() != null
                                ? categoryService.getCategoryById(productDto.categoryId())
                                : null)
                .imageUrl(productDto.image())
                .build();
    }
}
