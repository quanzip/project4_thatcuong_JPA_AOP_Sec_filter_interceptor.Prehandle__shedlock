package com.viettel.project.controller;

import com.viettel.project.common.HttpStatus;
import com.viettel.project.service.BillItemService;
import com.viettel.project.service.dto.BillItemDTO;
import com.viettel.project.service.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/billitem")
public class BillItemResource {

    @Autowired
    private BillItemService billItemService;

    @GetMapping(value = "")
    public ResponseDTO<List<BillItemDTO>> searchBillitems(@RequestParam(value = "userId", required = false) Long userId,
                                                          @RequestParam(value = "productId", required = false) Long productId,
                                                          @RequestParam(value = "fromDate", required = false) Date fromDate,
                                                          @RequestParam(value = "toDate", required = false) Date toDate,
                                                          @RequestParam(value = "minPrice", required = false) Double minPrice,
                                                          @RequestParam(value = "maxPrice", required = false) Double maxPrice,
                                                          @RequestParam(value = "page", required = false) Integer page,
                                                          @RequestParam(value = "size", required = false) Integer size) {
        List<BillItemDTO> res = billItemService.searchBillItem(userId, productId, fromDate, toDate, minPrice, maxPrice, page, size);
        return new ResponseDTO<List<BillItemDTO>>().status(HttpStatus.http_ok).message("Detting billItems ... done!").data(res);
    }

   @PostMapping()
    public ResponseDTO<BillItemDTO> saveBillItem(@RequestBody @Valid BillItemDTO billItemDTO) {
        BillItemDTO res = billItemService.saveBillItem(billItemDTO);
        return new ResponseDTO<BillItemDTO>().status(HttpStatus.http_ok).message("Saved billItem!").data(res);
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseDTO<Long> deleteById(@PathVariable(value = "id") Long id) {
        billItemService.deleteById(id);
        return new ResponseDTO<>().status(HttpStatus.http_ok).message("Deleted billItem byId: " + id).data(id);
    }

    @PostMapping(value = "update")
    public ResponseDTO<BillItemDTO> updateBillItem(@RequestBody BillItemDTO billItemDTO) {
        BillItemDTO res = billItemService.updateBillItem(billItemDTO);
        return new ResponseDTO<BillItemDTO>().status(HttpStatus.http_ok).message("Updated billItem id: " + billItemDTO.getId()).data(res);

    }
}
