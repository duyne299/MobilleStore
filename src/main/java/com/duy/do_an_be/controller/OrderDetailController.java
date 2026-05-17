package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Order;
import com.duy.do_an_be.entity.OrderDetail;
import com.duy.do_an_be.entity.ProductVariant;
import com.duy.do_an_be.repository.OrderDetailRepository;
import com.duy.do_an_be.repository.OrderRepository;
import com.duy.do_an_be.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-detail")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductVariantRepository productVariantRepository;

    @PostMapping
    public ResponseEntity<OrderDetail> create(@RequestBody OrderDetail orderDetail) {
        if (orderDetail.getOrder() != null && orderDetail.getOrder().getOrderId() != null) {
            Order order = orderRepository.findById(orderDetail.getOrder().getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            orderDetail.setOrder(order);
        }
        if (orderDetail.getOption() != null && orderDetail.getOption().getOptionId() != null) {
            ProductVariant variant = productVariantRepository.findById(orderDetail.getOption().getOptionId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found"));
            orderDetail.setOption(variant);
        }
        return ResponseEntity.ok(orderDetailRepository.save(orderDetail));
    }

    @GetMapping
    public ResponseEntity<List<OrderDetail>> findAll() {
        return ResponseEntity.ok(orderDetailRepository.findAll());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetail>> findByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderDetailRepository.findByOrderOrderId(orderId));
    }
}
