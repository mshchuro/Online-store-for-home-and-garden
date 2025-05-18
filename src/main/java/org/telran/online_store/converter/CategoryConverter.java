package org.telran.online_store.converter;

import org.springframework.stereotype.Component;
import org.telran.online_store.dto.CategoryRequestDto;
import org.telran.online_store.dto.CategoryResponseDto;
import org.telran.online_store.entity.Category;

@Component
public class CategoryConverter implements Converter<CategoryRequestDto, CategoryResponseDto, Category>{

    @Override
    public CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @Override
    public Category toEntity(CategoryRequestDto categoryDto) {
        return Category.builder()
                .name(categoryDto.name())
                .build();
    }
}
