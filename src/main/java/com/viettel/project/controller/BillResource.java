package com.viettel.project.controller;

import com.viettel.project.common.HttpStatus;
import com.viettel.project.service.BillService;
import com.viettel.project.service.dto.BillDTO;
import com.viettel.project.service.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/bill")
public class BillResource {
    @Autowired
    private BillService billService;

    @GetMapping(value = "")
    public ResponseDTO<List<BillDTO>> searchBills(@RequestParam(value = "fromDate", required = false) Date fromDate,
                                                  @RequestParam(value = "toDate", required = false) Date toDate,
                                                  @RequestParam(value = "page", required = false) Integer page,
                                                  @RequestParam(value = "size", required = false) Integer size) {
        List<BillDTO> res = billService.searchBills(fromDate, toDate, page, size);
        return new ResponseDTO<List<BillDTO>>().status(HttpStatus.http_ok).data(res).message("Getting all Bills done!");
    }

   @PostMapping()
    public ResponseDTO<BillDTO> addBill(@RequestBody BillDTO billDTO) {
        BillDTO res = billService.addBill(billDTO);
        return new ResponseDTO<BillDTO>().status(HttpStatus.http_ok).message("Added Bill").data(res);
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseDTO<Long> deleteBill(@PathVariable(value = "id") Long id) {
        billService.deleteBillById(id);
        return new ResponseDTO<Long>().status(HttpStatus.http_ok).message("Delete bill by id: " + id).data(id);
    }

    @PostMapping(value = "/update")
    public ResponseDTO<BillDTO> updateBill(@RequestBody BillDTO billDTO) {
        BillDTO res = billService.updateBill(billDTO);
        return new ResponseDTO<>().status(HttpStatus.http_ok).message("Updated bill id: " + billDTO.getId()).data(res);
    }

    @PostMapping("/bill-by-user/{id}")
    public ResponseDTO<BillDTO> getBillByUserId(@PathVariable(value = "id") Long userId) {
        BillDTO res = billService.getBillByUserId(userId);
        return new ResponseDTO<BillDTO>().status(HttpStatus.http_ok).message("Get bill by userId: " + userId).data(res);
    }

    @PostMapping(value = "/delete-by-user/{id}")
    public ResponseDTO<Long> deleteBillByUserId(@PathVariable(value = "id") Long userId) {
        Long billId = billService.deleteByUserId(userId);
        return new ResponseDTO<Long>().status(HttpStatus.http_ok).message("Delete bill by userId: " + userId).data(billId);
    }
}
