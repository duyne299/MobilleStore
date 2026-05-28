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

    @DeleteMapping("/{id}")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));
        Order order = orderDetail.getOrder();

        orderDetailRepository.delete(orderDetail);

        // Recalculate order totals based on remaining order details
        List<OrderDetail> remainingDetails = orderDetailRepository.findByOrderOrderId(order.getOrderId());
        double subTotal = remainingDetails.stream()
                .filter(d -> !d.getDetailId().equals(id))
                .mapToDouble(d -> d.getQuantity() * d.getPrice())
                .sum();

        double shippingFee = order.getShippingFee() != null ? order.getShippingFee() : 0.0;

        order.setTotalAmount(subTotal);
        order.setFinalAmount(subTotal + shippingFee);
        orderRepository.save(order);

        return ResponseEntity.noContent().build();
    }
}
