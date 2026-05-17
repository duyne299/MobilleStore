package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
