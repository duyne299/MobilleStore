package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findByOrderCodeContainingIgnoreCase(String orderCode, Pageable pageable);
    Page<Order> findByUserUserId(Long userId, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.details d WHERE o.user.userId = :userId AND d.option.product.proId = :proId AND o.status <> 'CANCELLED'")
    boolean existsByUserIdAndProIdAndNotCancelled(
        @org.springframework.data.repository.query.Param("userId") Long userId, 
        @org.springframework.data.repository.query.Param("proId") Long proId
    );

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT o FROM Order o JOIN o.details d WHERE d.option.product.proId = :proId")
    java.util.List<Order> findOrdersByProductId(@org.springframework.data.repository.query.Param("proId") Long proId);
}
