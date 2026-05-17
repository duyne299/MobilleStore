package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.User;
import com.duy.do_an_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Page<User> findAll(String search, int page, int limit) {
        if (search != null && !search.isEmpty()) {
            return userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, PageRequest.of(page - 1, limit));
        }
        return userRepository.findAll(PageRequest.of(page - 1, limit));
    }

    public User findOne(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User update(Long id, User userDetails) {
        User user = findOne(id);
        user.setFullName(userDetails.getFullName());
        user.setPhone(userDetails.getPhone());
        user.setAddress(userDetails.getAddress());
        if (userDetails.getAvatar() != null) user.setAvatar(userDetails.getAvatar());
        return userRepository.save(user);
    }

    public User updateStatus(Long id, boolean status) {
        User user = findOne(id);
        user.setStatus(status);
        return userRepository.save(user);
    }

    public User changeRole(Long id, String role) {
        User user = findOne(id);
        user.setRole(User.Role.valueOf(role));
        return userRepository.save(user);
    }

    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    public User getCurrentUser() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
