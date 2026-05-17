package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByCode(String code);
    Page<Discount> findByCodeContainingIgnoreCase(String code, Pageable pageable);
}
