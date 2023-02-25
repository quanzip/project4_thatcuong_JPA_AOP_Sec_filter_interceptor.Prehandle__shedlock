package com.viettel.project.service.mapper;

import com.viettel.project.entity.Category;
import com.viettel.project.service.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(source = "productDTOS", target = "products")
    Category toEntity(CategoryDTO productDTO);

    @Mapping(source = "products", target = "productDTOS")
    CategoryDTO toDTO(Category category);

    default Category fromId(Long id) {
        if (Objects.isNull(id)) return null;
        else {
            Category category = new Category();
            category.setId(id);
            return category;
        }
    }
}
