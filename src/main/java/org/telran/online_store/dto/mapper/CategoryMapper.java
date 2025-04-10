package org.telran.online_store.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.telran.online_store.dto.CategoryUpdateRequest;
import org.telran.online_store.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    void updateCategoryFromDto(CategoryUpdateRequest dto, @MappingTarget Category entity);

}
