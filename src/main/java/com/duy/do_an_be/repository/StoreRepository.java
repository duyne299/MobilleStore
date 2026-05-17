package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findByStoreNameContainingIgnoreCase(String name, Pageable pageable);
}
