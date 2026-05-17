package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserEmail(String email);
}
