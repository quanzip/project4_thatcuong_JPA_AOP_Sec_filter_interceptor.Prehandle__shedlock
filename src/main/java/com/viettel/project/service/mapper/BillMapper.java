package com.viettel.project.service.mapper;

import com.viettel.project.entity.Bill;
import com.viettel.project.service.dto.BillDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {BillItemMapper.class})
public interface BillMapper extends EntityMapper<BillDTO, Bill> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "billItems", target = "billItemDTOS")
    BillDTO toDTO(Bill bill);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "billItemDTOS", target = "billItems")
    Bill toEntity(BillDTO billDTO);

    default Bill fromId(Long id) {
        if (Objects.isNull(id)) return null;
        else {
            Bill bill = new Bill();
            bill.setId(id);
            return bill;
        }
    }
}
