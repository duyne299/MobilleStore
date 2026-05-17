package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.Cart;
import com.duy.do_an_be.entity.CartItem;
import com.duy.do_an_be.entity.ProductVariant;
import com.duy.do_an_be.entity.User;
import com.duy.do_an_be.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository variantRepository;

    public Cart getCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return cartRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    User user = userRepository.findByEmail(email).orElseThrow();
                    Cart newCart = Cart.builder().user(user).items(new ArrayList<>()).build();
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addToCart(Long optionId, Integer quantity) {
        Cart cart = getCart();
        ProductVariant variant = variantRepository.findById(optionId).orElseThrow();

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getOption().getOptionId().equals(optionId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .option(variant)
                    .quantity(quantity)
                    .isChecked(true)
                    .build();
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }
        return cart;
    }

    @Transactional
    public void updateQuantity(Long itemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    @Transactional
    public void updateItemCheck(Long itemId, boolean isChecked) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        item.setChecked(isChecked);
        cartItemRepository.save(item);
    }

    @Transactional
    public void updateAllItemsCheck(boolean isChecked) {
        Cart cart = getCart();
        cart.getItems().forEach(item -> item.setChecked(isChecked));
        cartItemRepository.saveAll(cart.getItems());
    }

    @Transactional
    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void clearCart() {
        Cart cart = getCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
