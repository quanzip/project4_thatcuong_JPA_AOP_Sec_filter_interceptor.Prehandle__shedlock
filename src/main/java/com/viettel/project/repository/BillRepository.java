package com.viettel.project.repository;

import com.viettel.project.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query(value = "select b from Bill b where b.buyDate >= :fromDate and b.buyDate <= :toDate")
    Page<Bill> searchByDateRage(@Param(value = "fromDate") Date fromDate, @Param(value = "toDate") Date toDate, Pageable pageable);

    Page<Bill> findAllByBuyDate(Date buyDate, Pageable pageable);

    Page<Bill> findAllByBuyDateGreaterThanEqualOrderByBuyDateAsc(Date fromDate, Pageable pageable);

    Page<Bill> findAllByBuyDateLessThanEqualOrderByBuyDateDesc(Date buyDate, Pageable pageable);

    Bill findByUserId(Long userId);
}
