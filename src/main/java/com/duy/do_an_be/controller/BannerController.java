package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Banner;
import com.duy.do_an_be.service.BannerService;
import com.duy.do_an_be.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit
    ) {
        Page<Banner> bannerPage = bannerService.findAll(search, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("data", bannerPage.getContent());
        response.put("total", bannerPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banner> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(bannerService.findOne(id));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Banner> create(
            @ModelAttribute Banner banner,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        if (image != null && !image.isEmpty()) {
            banner.setImageUrl(fileService.saveFile(image));
        }
        return ResponseEntity.ok(bannerService.create(banner));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Banner> update(
            @PathVariable Long id,
            @ModelAttribute Banner banner,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        if (image != null && !image.isEmpty()) {
            banner.setImageUrl(fileService.saveFile(image));
        }
        return ResponseEntity.ok(bannerService.update(id, banner));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        bannerService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Banner> updateStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(bannerService.updateStatus(id, body.get("isActive")));
    }
}
