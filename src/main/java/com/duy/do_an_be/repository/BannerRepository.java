package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    Page<Banner> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
