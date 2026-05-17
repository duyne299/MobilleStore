package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.User;
import com.duy.do_an_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final com.duy.do_an_be.service.FileService fileService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<User> userPage = userService.findAll(search, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("data", userPage.getContent());
        response.put("total", userPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findOne(id));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @ModelAttribute User user,
            @RequestPart(value = "avatar", required = false) org.springframework.web.multipart.MultipartFile avatar
    ) {
        if (avatar != null && !avatar.isEmpty()) {
            user.setAvatar(fileService.saveFile(avatar));
        }
        return ResponseEntity.ok(userService.update(id, user));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<User> updateStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(userService.updateStatus(id, body.get("status")));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<User> changeRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.changeRole(id, body.get("role")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        userService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
