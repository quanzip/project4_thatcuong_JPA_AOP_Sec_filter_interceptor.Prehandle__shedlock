package com.viettel.project.service;

import com.viettel.project.entity.Bill;
import com.viettel.project.service.dto.BillDTO;

import java.util.Date;
import java.util.List;

public interface BillService {
    List<BillDTO> searchBills(Date fromDate, Date toDate, Integer page, Integer size);

    BillDTO addBill(BillDTO billDTO);

    void deleteBillById(Long id);

    BillDTO updateBill(BillDTO billDTO);

    BillDTO getBillByUserId(Long userId);

    Long deleteByUserId(Long userId);

    Bill checkExistAndGetBill(Long billId);
}
