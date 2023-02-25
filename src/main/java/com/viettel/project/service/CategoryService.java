package com.viettel.project.service;

import com.viettel.project.service.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> searchCates(String search, Integer page, Integer size);

    CategoryDTO addCate(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(CategoryDTO categoryDTO);

    void deleteCategoryById(Long id);

}
