package com.viettel.project.service.dto;

/*W
 * this is the middle table of bill and product
 * because one product can be in many bills, one bill can have many product
 * */

import javax.validation.constraints.Min;

public class BillItemDTO extends BaseEntityDTO {
    private Long id;
    private Long productId;
    protected Long billId;

    @Min(value = 0)
    private Integer quantity;
    private Double totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "BillItemDTO{" +
                "id=" + id +
                ", productId=" + productId +
                ", billId=" + billId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
