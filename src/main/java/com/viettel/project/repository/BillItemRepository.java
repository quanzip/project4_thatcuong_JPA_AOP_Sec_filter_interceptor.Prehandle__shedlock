package com.viettel.project.repository;

import com.viettel.project.entity.BillItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Long> {

    @Modifying
    void deleteByBill_Id(Long billId);

    List<BillItem> findAllByQuantity(int quantity);

    Page<BillItem> findAllByProductIdAndBillIdAndTotalPriceBetweenAndBill_BuyDateBetween(Long productId, Long BillId,
                                                                                         Double fromPrice, Double toPrice,
                                                                                         Date fromDate, Date toDate, Pageable pageable);
}
