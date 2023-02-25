package com.viettel.project.controller;

import com.viettel.project.common.HttpStatus;
import com.viettel.project.service.ProductService;
import com.viettel.project.service.dto.ProductDTO;
import com.viettel.project.service.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductResource {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "")
    public ResponseDTO<List<ProductDTO>> searchProduct(@RequestParam(value = "search", required = false) String search,
                                                       @RequestParam(value = "page", required = false) Integer page
            , @RequestParam(value = "size", required = false) Integer size) {
        List<ProductDTO> result = productService.searchProduct(search, page, size);
        return new ResponseDTO<List<ProductDTO>>().status(HttpStatus.http_ok).message("Get products... done!").data(result);
    }

   @PostMapping()
    public ResponseDTO<ProductDTO> addProduct(@ModelAttribute @Valid ProductDTO productDTO) {
        ProductDTO res = productService.addProduct(productDTO);
        return new ResponseDTO<ProductDTO>().status(HttpStatus.http_ok).message("Added product name: " + productDTO.getName()).data(res);
    }

    @PostMapping(value = "/update")
    public ResponseDTO<Long> updateProduct(@ModelAttribute ProductDTO productDTO) {
        productService.updateProduct(productDTO);
        return new ResponseDTO<Long>().status(HttpStatus.http_ok).message("Updated product name: " + productDTO.getName()).data(productDTO.getId());
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseDTO<Long> deleteProductById(@PathVariable(value = "id") Long id) {
        productService.deleteProductById(id);
        return new ResponseDTO<>().status(HttpStatus.http_ok).message("Deleted product id: " + id).data(id);
    }
}
