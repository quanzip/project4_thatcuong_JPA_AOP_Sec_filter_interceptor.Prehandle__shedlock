package com.viettel.project.service.mapper;

import com.viettel.project.entity.BillItem;
import com.viettel.project.service.dto.BillItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {})
public interface BillItemMapper extends EntityMapper<BillItemDTO, BillItem> {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "bill.id", target = "billId")
    BillItemDTO toDTO(BillItem billItem);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "billId", target = "bill.id")
    BillItem toEntity(BillItemDTO billItemDTO);

    default BillItem fromId(Long id) {
        if (Objects.isNull(id)) return null;
        else {
            BillItem billItem = new BillItem();
            billItem.setId(id);
            return billItem;
        }
    }
}
