package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.Order;
import com.duy.do_an_be.entity.OrderDetail;
import com.duy.do_an_be.repository.OrderRepository;
import com.duy.do_an_be.repository.OrderDetailRepository;
import com.duy.do_an_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    public Long getSoldQuantityByProduct(Long productId) {
        Long sum = orderDetailRepository.sumQuantityByProductId(productId);
        return sum != null ? sum : 0L;
    }

    public Page<Order> findAll(Long userId, String status, String orderType, String dateFrom, String dateTo, String search, int page, int limit) {
        org.springframework.data.jpa.domain.Specification<Order> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("userId"), userId));
            }

            if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (orderType != null && !orderType.isEmpty() && !"all".equalsIgnoreCase(orderType)) {
                try {
                    Order.OrderType type = Order.OrderType.valueOf(orderType.toUpperCase());
                    predicates.add(cb.equal(root.get("orderType"), type));
                } catch (IllegalArgumentException e) {
                    // Ignore invalid enum
                }
            }

            if (dateFrom != null && !dateFrom.isEmpty()) {
                try {
                    java.time.LocalDateTime from = java.time.LocalDateTime.parse(dateFrom + "T00:00:00");
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
                } catch (Exception e) {
                    // Ignore
                }
            }

            if (dateTo != null && !dateTo.isEmpty()) {
                try {
                    java.time.LocalDateTime to = java.time.LocalDateTime.parse(dateTo + "T23:59:59");
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), to));
                } catch (Exception e) {
                    // Ignore
                }
            }

            if (search != null && !search.isEmpty()) {
                jakarta.persistence.criteria.Predicate codePredicate = cb.like(cb.lower(root.get("orderCode")), "%" + search.toLowerCase() + "%");
                jakarta.persistence.criteria.Join<Order, com.duy.do_an_be.entity.User> userJoin = root.join("user", jakarta.persistence.criteria.JoinType.LEFT);
                jakarta.persistence.criteria.Predicate namePredicate = cb.like(cb.lower(userJoin.get("fullName")), "%" + search.toLowerCase() + "%");
                predicates.add(cb.or(codePredicate, namePredicate));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        return orderRepository.findAll(spec, PageRequest.of(page - 1, limit, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt")));
    }

    public Order findOne(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order create(Order order) {
        if (order.getOrderCode() == null) {
            order.setOrderCode("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Resolve user from either payload or current authenticated security context
        if (order.getUser() != null && order.getUser().getUserId() != null) {
            userRepository.findById(order.getUser().getUserId()).ifPresent(order::setUser);
        } else {
            try {
                org.springframework.security.core.Authentication auth = 
                    org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                    String email = auth.getName();
                    userRepository.findByEmail(email).ifPresent(order::setUser);
                }
            } catch (Exception e) {
                // Fallback / ignore
            }
        }

        if (order.getDetails() != null) {
            for (OrderDetail detail : order.getDetails()) {
                detail.setOrder(order);
            }
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateStatus(Long id, Order orderDto) {
        Order order = findOne(id);
        if (orderDto.getStatus() != null) {
            order.setStatus(orderDto.getStatus());
        }
        if (orderDto.getTransactionCode() != null) {
            order.setTransactionCode(orderDto.getTransactionCode());
        }
        return orderRepository.save(order);
    }

    @Transactional
    public void remove(Long id) {
        orderRepository.deleteById(id);
    }
}
