package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findBySlug(String slug);
    Page<Brand> findByBrandNameContainingIgnoreCase(String brandName, Pageable pageable);
    boolean existsByBrandName(String brandName);
}
