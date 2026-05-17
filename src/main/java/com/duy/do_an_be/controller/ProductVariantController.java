package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Product;
import com.duy.do_an_be.entity.ProductVariant;
import com.duy.do_an_be.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product-options")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService variantService;

    @GetMapping
    public ResponseEntity<List<ProductVariant>> getAll() {
        return ResponseEntity.ok(variantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<ProductVariant> create(@RequestBody ProductVariant variant) {
        return ResponseEntity.ok(variantService.create(variant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVariant> update(@PathVariable Long id, @RequestBody ProductVariant variant) {
        return ResponseEntity.ok(variantService.update(id, variant));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductVariant> updateStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(variantService.updateStatus(id, body.get("isActive")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        variantService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{proId}")
    public ResponseEntity<List<ProductVariant>> getByProductId(@PathVariable Long proId) {
        return ResponseEntity.ok(variantService.findByProductId(proId));
    }

    @GetMapping("/{id}/product")
    public ResponseEntity<Product> getProductByOptionId(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.findOne(id).getProduct());
    }
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductVariant> updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(variantService.updateStock(id, body.get("quantityAdjustment")));
    }
}
