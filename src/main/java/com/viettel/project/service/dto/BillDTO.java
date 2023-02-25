package com.viettel.project.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class BillDTO extends BaseEntityDTO {
    private Long id;

    //  khi truyen tu form cua FE xuong BE: voi @ModelAttribute (thuong form co file)
    @DateTimeFormat(pattern = "dd/MM/YYYY HH:mm")
//    khi tra ve/nhan data tu form voi @RequestBody:
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date buyDate;

    private Long userId;

    private List<BillItemDTO> billItemDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<BillItemDTO> getBillItemDTOS() {
        return billItemDTOS;
    }

    public void setBillItemDTOS(List<BillItemDTO> billItemDTOS) {
        this.billItemDTOS = billItemDTOS;
    }
}
