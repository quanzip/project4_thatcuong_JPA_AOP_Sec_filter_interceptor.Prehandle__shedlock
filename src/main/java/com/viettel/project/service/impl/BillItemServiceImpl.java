package com.viettel.project.service.impl;

import com.viettel.project.entity.BillItem;
import com.viettel.project.repository.BillItemRepository;
import com.viettel.project.service.BillItemService;
import com.viettel.project.service.BillService;
import com.viettel.project.service.ProductService;
import com.viettel.project.service.dto.BillItemDTO;
import com.viettel.project.service.mapper.BillItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

@Service
public class BillItemServiceImpl implements BillItemService {
    private final Logger logger = LoggerFactory.getLogger(BillItemServiceImpl.class);

    @Autowired
    private BillItemMapper billItemMapper;

    @Autowired
    private BillItemRepository billItemRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductService productService;

    @Autowired
    private BillService billService;

    @Override
    @Transactional
    public void deleteBybillId(Long id) {
        logger.info("Deleting billitems by id: " + id);
        billItemRepository.deleteByBill_Id(id);
    }

    @Override
    public List<BillItemDTO> searchBillItem(Long userId, Long productId,
                                            Date fromDate, Date toDate,
                                            Double minPrice, Double maxPrice, Integer page, Integer size) {

        List<BillItemDTO> res;
        Pageable pageable = PageRequest.of(Objects.isNull(page) ? 0 : page, Objects.isNull(size) ? 10 : size);

        if (!Objects.isNull(userId) && !Objects.isNull(productId)
                && !Objects.isNull(fromDate) && !Objects.isNull(toDate)
                && !Objects.isNull(minPrice) && !Objects.isNull(maxPrice)) {
            Page<BillItem> billItems = billItemRepository.findAllByProductIdAndBillIdAndTotalPriceBetweenAndBill_BuyDateBetween(
                    productId, userId, minPrice, maxPrice, fromDate, toDate, pageable);
            res = billItemMapper.toDTOS(billItems.getContent());
        } else {
            Map<String, Object> params = new HashMap<>();
            StringBuilder stringBuilder = new StringBuilder("select bill_item.* from bill_item inner join bill on bill.id = bill_item.bill_id " +
                    " inner join product on product.id = bill_item.product_id where 1 = 1 ");

            // userId and productId
            if (!Objects.isNull(userId)) {
                stringBuilder.append(" and bill.user_id = :userId ");
                params.put("userId", userId);
            }

            if (!Objects.isNull(productId)) {
                stringBuilder.append(" and  product.id == :productId ");
                params.put("productId", productId);
            }

            // buy_date
            if (!Objects.isNull(fromDate) && !Objects.isNull(toDate)) {
                stringBuilder.append(" and bill.buy_date >= :fromDate and bill.buy_date <= :toDate ");
                params.put("fromDate", fromDate);
                params.put("toDate", toDate);
            } else if (!Objects.isNull(fromDate)) {
                stringBuilder.append(" and bill.buy_date >= :fromDate ");
                params.put("fromDate", fromDate);
            } else if(!Objects.isNull(toDate)) {
                stringBuilder.append(" and bill.buy_date <= :toDate ");
                params.put("toDate", toDate);
            }

            // price
            if (!Objects.isNull(minPrice) && !Objects.isNull(maxPrice)) {
                stringBuilder.append(" and total_price >= :minPrice and total_price <= :maxPrice ");
                params.put("minPrice", minPrice);
                params.put("maxPrice", maxPrice);
            } else if (!Objects.isNull(minPrice)) {
                stringBuilder.append(" and total_price >= :minPrice ");
                params.put("minPrice", minPrice);
            } else if(!Objects.isNull(maxPrice)) {
                stringBuilder.append(" and total_price <= :maxPrice ");
                params.put("maxPrice", maxPrice);
            }

            Query query = entityManager.createNativeQuery(stringBuilder.toString(), BillItem.class);

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            List<BillItem> roughResult = query
                    .setFirstResult(pageable.getPageNumber())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
            res = billItemMapper.toDTOS(roughResult);
        }
        return res;
    }

    @Override
    public BillItemDTO saveBillItem(BillItemDTO billItemDTO) {
        if (billItemDTO == null) throw new NullPointerException("Can not save null BillItem!");
        productService.checkExistAndGetProduct(billItemDTO.getProductId());
        billService.checkExistAndGetBill(billItemDTO.getBillId());
        BillItemDTO res = billItemMapper.toDTO(billItemRepository.save(billItemMapper.toEntity(billItemDTO)));
        return res;
    }

    @Override
    public void deleteById(Long id) {
        BillItem billItem = checkExistAndGetBillItem(id);
        billItemRepository.delete(billItem);
    }

    @Override
    public BillItemDTO updateBillItem(BillItemDTO billItemDTO) {
        if (billItemDTO == null) throw new NullPointerException("Can not save null BillItem!");
        productService.checkExistAndGetProduct(billItemDTO.getProductId());
        billService.checkExistAndGetBill(billItemDTO.getBillId());

        // update quantity -> update total price for bill item
        BillItemDTO res = billItemMapper.toDTO(billItemRepository.save(billItemMapper.toEntity(billItemDTO)));
        return res;
    }

    @Override
    public List<BillItemDTO> deleteBillItemThatHasZeroQuantity() {
        List<BillItem> itemsThatHasZeroQuantity = billItemRepository.findAllByQuantity(0);
        billItemRepository.deleteAll(itemsThatHasZeroQuantity);
        return billItemMapper.toDTOS(itemsThatHasZeroQuantity);
    }

    private BillItem checkExistAndGetBillItem(Long id) {
        return billItemRepository.findById(id).orElseThrow(() -> new NoResultException("Non-exist bill item by id: " + id));
    }

}
