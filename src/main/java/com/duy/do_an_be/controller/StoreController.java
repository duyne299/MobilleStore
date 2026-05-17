package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Store;
import com.duy.do_an_be.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<Store> storePage = storeService.findAll(search, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("data", storePage.getContent());
        response.put("total", storePage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Store> create(@RequestBody Store store) {
        return ResponseEntity.ok(storeService.create(store));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> update(@PathVariable Long id, @RequestBody Store store) {
        return ResponseEntity.ok(storeService.update(id, store));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        storeService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Store> updateStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(storeService.updateStatus(id, body.get("isActive")));
    }
}
