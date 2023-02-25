package com.viettel.project.service;

import com.viettel.project.entity.Product;
import com.viettel.project.service.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> searchProduct(String search, Integer page, Integer size);

    ProductDTO addProduct(ProductDTO productDTO);

    void updateProduct(ProductDTO productDTO);

    void deleteProductById(Long id);

    Product checkExistAndGetProduct(Long productId);
}
