package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Product;
import com.duy.do_an_be.service.FileService;
import com.duy.do_an_be.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("images");
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<Product> productPage = productService.findAll(search, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("data", productPage.getContent());
        response.put("total", productPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Product> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.findBySlug(slug));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> findByCategory(@PathVariable Long categoryId) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", productService.findByCategory(categoryId));
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Product> create(
            @ModelAttribute Product product,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : images) {
                imageUrls.add(fileService.saveFile(file));
            }
            product.setImages(imageUrls);
            if (!imageUrls.isEmpty()) {
                product.setMainImage(imageUrls.get(0));
            }
        }
        return ResponseEntity.ok(productService.create(product));
    }

    @PutMapping(value = "/{slug}", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> update(
            @PathVariable String slug,
            @ModelAttribute Product product,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : images) {
                imageUrls.add(fileService.saveFile(file));
            }
            product.setImages(imageUrls);
        }
        return ResponseEntity.ok(productService.update(slug, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        productService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Product> changeStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(productService.changeStatus(id, body.get("isActive")));
    }
}
