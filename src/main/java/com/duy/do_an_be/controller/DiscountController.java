package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Discount;
import com.duy.do_an_be.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<Discount> discountPage = discountService.findAll(search, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("data", discountPage.getContent());
        response.put("total", discountPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Discount> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(discountService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Discount> create(@RequestBody Discount discount) {
        return ResponseEntity.ok(discountService.create(discount));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Discount> update(@PathVariable Long id, @RequestBody Discount discount) {
        return ResponseEntity.ok(discountService.update(id, discount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        discountService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/apply/code")
    public ResponseEntity<Map<String, Object>> apply(
            @RequestParam String code,
            @RequestParam Double total
    ) {
        try {
            Discount discount = discountService.applyDiscount(code, total);
            Double discountAmount = 0.0;
            if ("PERCENT".equalsIgnoreCase(discount.getDiscountType())) {
                discountAmount = (total * discount.getValue()) / 100.0;
            } else if ("AMOUNT".equalsIgnoreCase(discount.getDiscountType())) {
                discountAmount = discount.getValue();
            }
            if (discountAmount > total) {
                discountAmount = total;
            }
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("discountAmount", discountAmount);
            response.put("discount", discount);
            response.put("message", "Áp dụng mã giảm giá thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Discount> updateStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(discountService.updateStatus(id, body.get("isActive")));
    }
}
