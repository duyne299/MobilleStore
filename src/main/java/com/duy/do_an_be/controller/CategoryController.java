package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Category;
import com.duy.do_an_be.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final com.duy.do_an_be.service.FileService fileService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<Category> categoryPage = categoryService.findAll(search, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("data", categoryPage.getContent());
        response.put("total", categoryPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idOrSlug}")
    public ResponseEntity<Category> findOne(@PathVariable String idOrSlug) {
        return ResponseEntity.ok(categoryService.findOne(idOrSlug));
    }

    @PostMapping
    public ResponseEntity<Category> create(
            @ModelAttribute Category category,
            @RequestParam(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    ) {
        if (image != null && !image.isEmpty()) {
            category.setCategoryImage(fileService.saveFile(image));
        }
        return ResponseEntity.ok(categoryService.create(category));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Category> update(
            @PathVariable String slug,
            @ModelAttribute Category category,
            @RequestParam(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    ) {
        if (image != null && !image.isEmpty()) {
            category.setCategoryImage(fileService.saveFile(image));
        }
        return ResponseEntity.ok(categoryService.update(slug, category));
    }

    @PatchMapping("/{slug}/status")
    public ResponseEntity<Category> updateStatus(@PathVariable String slug, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(categoryService.updateStatus(slug, body.get("isActive")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        categoryService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-name")
    public ResponseEntity<Map<String, Boolean>> checkName(@RequestParam String name) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", categoryService.checkName(name));
        return ResponseEntity.ok(response);
    }
}
