package com.duy.do_an_be.controller;

import com.duy.do_an_be.entity.Cart;
import com.duy.do_an_be.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> body) {
        Long optionId = Long.valueOf(body.get("optionId").toString());
        Integer quantity = Integer.valueOf(body.get("quantity").toString());
        Cart cart = cartService.addToCart(optionId, quantity);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product added to cart");
        response.put("cartId", cart.getCartId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Map<String, Object>> updateQuantity(@PathVariable Long itemId, @RequestBody Map<String, Integer> body) {
        cartService.updateQuantity(itemId, body.get("quantity"));
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Quantity updated");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{itemId}/check")
    public ResponseEntity<Map<String, Object>> updateItemCheck(@PathVariable Long itemId, @RequestBody Map<String, Boolean> body) {
        cartService.updateItemCheck(itemId, body.get("isChecked"));
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item check status updated");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/check-all")
    public ResponseEntity<Map<String, Object>> updateAllItemsCheck(@RequestBody Map<String, Boolean> body) {
        cartService.updateAllItemsCheck(body.get("isChecked"));
        Map<String, Object> response = new HashMap<>();
        response.put("message", "All items check status updated");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Map<String, Object>> removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item removed from cart");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> clearCart() {
        cartService.clearCart();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart cleared");
        return ResponseEntity.ok(response);
    }
}
