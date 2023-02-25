package com.viettel.project.service;

import com.viettel.project.service.dto.BillItemDTO;

import java.util.Date;
import java.util.List;

public interface BillItemService {
    void deleteBybillId(Long id);

    List<BillItemDTO> searchBillItem(Long userId, Long productId, Date fromDate, Date toDate, Double minPrice, Double maxPrice, Integer page, Integer size);

    BillItemDTO saveBillItem(BillItemDTO billItemDTO);

    void deleteById(Long id);

    BillItemDTO updateBillItem(BillItemDTO billItemDTO);

    List<BillItemDTO> deleteBillItemThatHasZeroQuantity();
}
