package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    Page<Category> findByCategoryNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByCategoryName(String name);
}
