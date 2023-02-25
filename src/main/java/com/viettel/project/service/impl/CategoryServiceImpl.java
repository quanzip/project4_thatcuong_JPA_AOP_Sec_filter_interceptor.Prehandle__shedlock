package com.viettel.project.service.impl;

import com.viettel.project.entity.Category;
import com.viettel.project.repository.CategoryRepository;
import com.viettel.project.service.dto.CategoryDTO;
import com.viettel.project.service.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements com.viettel.project.service.CategoryService {
    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> searchCates(String search, Integer page, Integer size) {
        logger.info("Getting categories... done!");
        List<CategoryDTO> res;
        Pageable pageable = PageRequest.of(Objects.isNull(page) ? 0 : page, Objects.isNull(size) ? 10 : size);
        if (Objects.isNull(search) || search.isEmpty()) {
            res = categoryMapper.toDTOS(categoryRepository.findAll(pageable).getContent());
        } else {
            res = categoryMapper.toDTOS(categoryRepository.findCategoriesByName("%" + search + "%", pageable).getContent());
        }
        logger.info("Getting categories... done!");
        return res;
    }

    @Override
    public CategoryDTO addCate(CategoryDTO categoryDTO) {
        if (Objects.isNull(categoryDTO)) throw new NullPointerException("Can not save null category!");
        Category category = categoryMapper.toEntity(categoryDTO);
        categoryRepository.save(category);
        logger.info("Added category: " + categoryDTO.getName());
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        if (Objects.isNull(categoryDTO)) throw new NullPointerException("Can not update null category!");
        Category category = categoryMapper.toEntity(categoryDTO);
        categoryRepository.save(category);
        logger.info("Updated category: " + categoryDTO.getName());

        return categoryMapper.toDTO(category);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new NoResultException("Can not find category by id to delete: " + id));
        categoryRepository.deleteById(id);
        logger.info("Deleted category id: " + id);
    }
}
