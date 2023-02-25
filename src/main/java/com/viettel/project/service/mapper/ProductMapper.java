package com.viettel.project.service.mapper;

import com.viettel.project.entity.Product;
import com.viettel.project.service.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(uses = {BillItemMapper.class}, componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(source = "billItems", target = "billItemDTOS")
    @Mapping(source = "category.id", target = "categoryId")
    ProductDTO toDTO(Product product);

    @Mapping(source = "billItemDTOS", target = "billItems")
    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id) {
        if (Objects.isNull(id)) return null;
        else {
            Product product = new Product();
            product.setId(id);
            return product;
        }
    }
}
