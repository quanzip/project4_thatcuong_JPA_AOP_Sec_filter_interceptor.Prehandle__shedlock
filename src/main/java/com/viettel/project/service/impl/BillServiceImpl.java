package com.viettel.project.service.impl;

import com.viettel.project.entity.Bill;
import com.viettel.project.repository.BillRepository;
import com.viettel.project.service.BillItemService;
import com.viettel.project.service.BillService;
import com.viettel.project.service.UserService;
import com.viettel.project.service.dto.BillDTO;
import com.viettel.project.service.mapper.BillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BillServiceImpl implements BillService {
    private final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private BillItemService billItemService;

    @Override
    public List<BillDTO> searchBills(Date fromDate, Date toDate, Integer page, Integer size) {
        List<BillDTO> res;
        Pageable pageable = PageRequest.of(Objects.isNull(page) ? 0 : page, Objects.isNull(size) ? 10 : size);
        boolean isFromDateNull = Objects.isNull(fromDate);
        boolean isToDateNull = Objects.isNull(toDate);
        if (isFromDateNull && isToDateNull) {
            res = billMapper.toDTOS(billRepository.findAll(pageable).getContent());
        } else {
            if (!isFromDateNull && !isToDateNull) {
                res = billMapper.toDTOS(billRepository.searchByDateRage(fromDate, toDate, pageable).getContent());
            } else if (!isFromDateNull) {
                res = billMapper.toDTOS(billRepository.findAllByBuyDateGreaterThanEqualOrderByBuyDateAsc(fromDate, pageable).getContent());
            } else {   // !isToDateNull
                res = billMapper.toDTOS(billRepository.findAllByBuyDateLessThanEqualOrderByBuyDateDesc(toDate, pageable).getContent());
            }
        }
        logger.info("Getting bills... done!");
        return res;
    }

    @Override
    public BillDTO addBill(BillDTO billDTO) {
        if (Objects.isNull(billDTO)) throw new NullPointerException("Can not save null bill");

        // check user id exist?
        Long userId = billDTO.getUserId();
        userService.findUserById(userId).orElseThrow(() -> new NoResultException("Could not found user id: " + userId + " to save bill"));

        //save bill
        Bill bill = billMapper.toEntity(billDTO);
        billRepository.save(bill);
        return billMapper.toDTO(bill);
    }

    @Override
    public void deleteBillById(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new NoResultException("Can not delete non-exist bill"));

        //delete Bill Item first
        billItemService.deleteBybillId(bill.getId());

        // deleteBill
        billRepository.delete(bill);
    }

    @Override
    public BillDTO updateBill(BillDTO billDTO) {
        if (billDTO == null || !billRepository.findById(billDTO.getId()).isPresent()) {
            throw new NoResultException("Can not update null or Not-found bill");
        }
        //check userId exist ?
        userService.findUserById(billDTO.getUserId()).orElseThrow(() -> new NoResultException("Can not delete bill because non-exist user"));

        // only update buyDate
        Bill bill = billMapper.toEntity(billDTO);
        billRepository.save(bill);
        return billMapper.toDTO(bill);
    }

    @Override
    public BillDTO getBillByUserId(Long userId) {
        //check user exist
        userService.getUserById(userId);

        Bill bill = billRepository.findByUserId(userId);
        return billMapper.toDTO(bill);
    }

    @Override
    public Long deleteByUserId(Long userId) {
        // check user exist
        userService.getUserById(userId);

        Bill bill = billRepository.findByUserId(userId);
        if (bill == null) throw new NoResultException("User id: " + userId + " does not have any bill!");
        billRepository.delete(bill);
        return bill.getId();
    }

    @Override
    public Bill checkExistAndGetBill(Long billId) {
        return billRepository.findById(billId).orElseThrow(() -> new NoResultException("Non-exist bill by id: " + billId));
    }
}
