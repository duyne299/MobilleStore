package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"variants", "category", "brand"})
    Optional<Product> findBySlug(String slug);

    @EntityGraph(attributePaths = {"variants", "category", "brand"})
    Page<Product> findByProNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"variants", "category", "brand"})
    List<Product> findByCategoryCategoryId(Long categoryId);

    @Override
    @EntityGraph(attributePaths = {"variants", "category", "brand"})
    Page<Product> findAll(Pageable pageable);
}
