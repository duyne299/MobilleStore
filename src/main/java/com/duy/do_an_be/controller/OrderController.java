package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Order;
import com.duy.do_an_be.entity.User;
import com.duy.do_an_be.service.OrderService;
import com.duy.do_an_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.STAFF) {
            userId = currentUser.getUserId();
        }

        Page<Order> orderPage = orderService.findAll(userId, status, orderType, dateFrom, dateTo, search, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("orders", orderPage.getContent());
        response.put("total", orderPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.create(order));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateStatus(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        orderService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sold-quantity/{proId}")
    public ResponseEntity<Map<String, Long>> getSoldQuantity(@PathVariable Long proId) {
        Map<String, Long> response = new HashMap<>();
        response.put("soldQuantity", orderService.getSoldQuantityByProduct(proId));
        return ResponseEntity.ok(response);
    }
}
