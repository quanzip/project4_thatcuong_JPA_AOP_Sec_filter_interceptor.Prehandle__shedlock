package com.viettel.project.service.dto;

import javax.validation.constraints.Size;
import java.util.List;

public class CategoryDTO extends BaseEntityDTO {
    private Long id;

    @Size(min = 3)
    private String name;

    private List<ProductDTO> productDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductDTO> getProductDTOS() {
        return productDTOS;
    }

    public void setProductDTOS(List<ProductDTO> productDTOS) {
        this.productDTOS = productDTOS;
    }
}
