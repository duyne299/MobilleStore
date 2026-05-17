package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByContentContainingIgnoreCase(String content, Pageable pageable);
    @org.springframework.data.jpa.repository.Query("SELECT r FROM Review r LEFT JOIN FETCH r.user LEFT JOIN FETCH r.product WHERE r.product.proId = :proId AND r.isActive = true ORDER BY r.createdAt DESC")
    List<Review> findByProductProId(@org.springframework.data.repository.query.Param("proId") Long proId);

    @org.springframework.data.jpa.repository.Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.proId = :proId AND r.rating > 0")
    Double calculateAverageRatingByProId(Long proId);

    Long countByProductProIdAndRatingGreaterThan(Long proId, Integer rating);
}
