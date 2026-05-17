package com.duy.do_an_be.repository;

import com.duy.do_an_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    org.springframework.data.domain.Page<User> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String fullName, String email, org.springframework.data.domain.Pageable pageable);
}
