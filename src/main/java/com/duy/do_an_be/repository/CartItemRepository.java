package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
