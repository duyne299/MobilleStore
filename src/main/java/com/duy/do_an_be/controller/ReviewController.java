package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Review;
import com.duy.do_an_be.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final com.duy.do_an_be.service.FileService fileService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<Review> reviewPage = reviewService.findAll(search, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("data", reviewPage.getContent());
        response.put("total", reviewPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{proId}")
    public ResponseEntity<List<Review>> findByProductId(@PathVariable Long proId) {
        return ResponseEntity.ok(reviewService.findByProductId(proId));
    }

    @GetMapping("/rating/{proId}")
    public ResponseEntity<Map<String, Object>> getRating(@PathVariable Long proId) {
        return ResponseEntity.ok(reviewService.getRating(proId));
    }

    @GetMapping("/check-purchase/{proId}")
    public ResponseEntity<Map<String, Boolean>> checkPurchase(@PathVariable Long proId) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasPurchased", reviewService.checkPurchase(proId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findOne(id));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Review> create(
            @ModelAttribute Review review,
            @RequestParam(value = "proId", required = false) Long proId,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestPart(value = "images", required = false) List<org.springframework.web.multipart.MultipartFile> images
    ) {
        if (images != null && !images.isEmpty()) {
            java.util.List<String> imageUrls = new java.util.ArrayList<>();
            for (org.springframework.web.multipart.MultipartFile file : images) {
                imageUrls.add(fileService.saveFile(file));
            }
            review.setReviewImages(imageUrls);
        }
        return ResponseEntity.ok(reviewService.create(review, proId, comment));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Review> update(
            @PathVariable Long id,
            @ModelAttribute Review review,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestPart(value = "images", required = false) List<org.springframework.web.multipart.MultipartFile> images
    ) {
        if (comment != null && !comment.trim().isEmpty()) {
            review.setContent(comment);
        }
        if (images != null && !images.isEmpty()) {
             java.util.List<String> imageUrls = new java.util.ArrayList<>();
            for (org.springframework.web.multipart.MultipartFile file : images) {
                imageUrls.add(fileService.saveFile(file));
            }
            review.setReviewImages(imageUrls);
        }
        return ResponseEntity.ok(reviewService.update(id, review));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Review> updateStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(reviewService.updateStatus(id, body.get("isActive")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        reviewService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
