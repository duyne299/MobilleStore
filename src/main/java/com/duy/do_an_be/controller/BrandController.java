package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Brand;
import com.duy.do_an_be.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final com.duy.do_an_be.service.FileService fileService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<Brand> brandPage = brandService.findAll(search, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("data", brandPage.getContent());
        response.put("total", brandPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.findOne(id));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Brand> create(
            @RequestPart("data") Brand brand,
            @RequestPart(value = "logo", required = false) org.springframework.web.multipart.MultipartFile logo
    ) {
        if (logo != null && !logo.isEmpty()) {
            brand.setBrandLogo(fileService.saveFile(logo));
        }
        return ResponseEntity.ok(brandService.create(brand));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> update(@PathVariable Long id, @RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.update(id, brand));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        brandService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{slug}/status")
    public ResponseEntity<Brand> updateStatus(@PathVariable String slug, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(brandService.updateStatus(slug, body.get("isActive")));
    }

    @GetMapping("/check-name")
    public ResponseEntity<Map<String, Boolean>> checkName(@RequestParam String name) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", brandService.checkName(name));
        return ResponseEntity.ok(response);
    }
}
