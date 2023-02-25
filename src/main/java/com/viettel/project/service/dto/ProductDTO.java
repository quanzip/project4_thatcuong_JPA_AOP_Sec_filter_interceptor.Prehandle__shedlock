package com.viettel.project.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

public class ProductDTO extends BaseEntityDTO {
    private Long id;

    @Size(min = 3)
    private String name;
    private Long categoryId;
    @Min(value = 0)
    private Double price;
    private String description;
    private String image;
    private List<BillItemDTO> billItemDTOS;

    // @Transient use this wwhen we want this property does not appear in table but till in Object
    @JsonIgnore // Khi tra ra data cho FE, se bo qua data cua field nay
    private MultipartFile file;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<BillItemDTO> getBillItemDTOS() {
        return billItemDTOS;
    }

    public void setBillItemDTOS(List<BillItemDTO> billItemDTOS) {
        this.billItemDTOS = billItemDTOS;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", billItemDTOS=" + billItemDTOS +
                ", file=" + file +
                '}';
    }
}
