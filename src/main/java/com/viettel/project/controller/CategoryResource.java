package com.viettel.project.controller;

import com.viettel.project.common.HttpStatus;
import com.viettel.project.service.CategoryService;
import com.viettel.project.service.dto.CategoryDTO;
import com.viettel.project.service.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/category")
public class CategoryResource {
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "")
    public ResponseDTO<List<CategoryDTO>> searchCategory(@RequestParam(value = "search", required = false) String search,
                                                         @RequestParam(value = "page", required = false) Integer page,
                                                         @RequestParam(value = "size", required = false) Integer size) {
        List<CategoryDTO> res = categoryService.searchCates(search, page, size);
        return new ResponseDTO<List<CategoryDTO>>().status(HttpStatus.http_ok).message("Getting all categories done!").data(res);
    }

    @PostMapping(value = "")
    public ResponseDTO<CategoryDTO> addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryDTO res = categoryService.addCate(categoryDTO);
        return new ResponseDTO<>().status(HttpStatus.http_ok).message("Added category: " + categoryDTO.getName()).data(res);
    }

    @PostMapping(value = "/update")
    public ResponseDTO<CategoryDTO> updateCate(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO res = categoryService.updateCategory(categoryDTO);
        return new ResponseDTO<CategoryDTO>().status(HttpStatus.http_ok).message("Updated category name: " + categoryDTO.getName()).data(res);
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseDTO<Long> deleteCateById(@PathVariable(value = "id") Long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseDTO<Long>().status(HttpStatus.http_ok).message("Deleted category id: " + id).data(id);
    }
}
