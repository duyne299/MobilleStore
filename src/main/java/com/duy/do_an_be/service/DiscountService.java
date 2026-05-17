package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.Discount;
import com.duy.do_an_be.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;

    public Page<Discount> findAll(String search, int page, int limit) {
        if (search != null && !search.isEmpty()) {
            return discountRepository.findByCodeContainingIgnoreCase(search, PageRequest.of(page - 1, limit));
        }
        return discountRepository.findAll(PageRequest.of(page - 1, limit));
    }

    public Discount findOne(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
    }

    public Discount findByCode(String code) {
        return discountRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Discount code not found"));
    }

    @Transactional
    public Discount create(Discount discount) {
        return discountRepository.save(discount);
    }

    @Transactional
    public Discount update(Long id, Discount discountDto) {
        Discount discount = findOne(id);
        discount.setCode(discountDto.getCode());
        discount.setDescription(discountDto.getDescription());
        discount.setDiscountType(discountDto.getDiscountType());
        discount.setValue(discountDto.getValue());
        discount.setStartDate(discountDto.getStartDate());
        discount.setEndDate(discountDto.getEndDate());
        discount.setUsageLimit(discountDto.getUsageLimit());
        discount.setMinOrderValue(discountDto.getMinOrderValue());
        discount.setActive(discountDto.isActive());
        return discountRepository.save(discount);
    }

    @Transactional
    public void remove(Long id) {
        discountRepository.deleteById(id);
    }

    @Transactional
    public Discount updateStatus(Long id, boolean isActive) {
        Discount discount = findOne(id);
        discount.setActive(isActive);
        return discountRepository.save(discount);
    }

    public Discount applyDiscount(String code, Double total) {
        Discount discount = findByCode(code);
        if (!discount.isActive()) {
            throw new RuntimeException("Mã giảm giá này đã ngừng hoạt động!");
        }
        if (discount.getStartDate() != null && discount.getStartDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Mã giảm giá này chưa đến thời gian áp dụng!");
        }
        if (discount.getEndDate() != null && discount.getEndDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã giảm giá này đã hết hạn sử dụng!");
        }
        if (discount.getUsageLimit() != null && discount.getUsageLimit() <= 0) {
            throw new RuntimeException("Mã giảm giá này đã hết lượt sử dụng!");
        }
        if (discount.getMinOrderValue() != null && total < discount.getMinOrderValue()) {
            throw new RuntimeException("Đơn hàng tối thiểu phải từ " + discount.getMinOrderValue() + " để áp dụng mã!");
        }
        return discount;
    }
}
