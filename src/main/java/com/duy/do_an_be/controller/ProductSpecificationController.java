package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.ProductSpecification;
import com.duy.do_an_be.service.ProductSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-specification")
@RequiredArgsConstructor
public class ProductSpecificationController {

    private final ProductSpecificationService service;

    @GetMapping
    public ResponseEntity<List<ProductSpecification>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSpecification> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOne(id));
    }

    @PostMapping
    public ResponseEntity<ProductSpecification> create(@RequestBody ProductSpecification spec) {
        return ResponseEntity.ok(service.create(spec));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductSpecification> update(@PathVariable Long id, @RequestBody ProductSpecification spec) {
        return ResponseEntity.ok(service.update(id, spec));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
