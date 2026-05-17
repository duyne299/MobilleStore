package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.Product;
import com.duy.do_an_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<Product> findAll(String search, int page, int limit) {
        Page<Product> productPage;
        if (search != null && !search.isEmpty()) {
            productPage = productRepository.findByProNameContainingIgnoreCase(search, PageRequest.of(page - 1, limit));
        } else {
            productPage = productRepository.findAll(PageRequest.of(page - 1, limit));
        }
        productPage.getContent().forEach(p -> {
            if (p.getVariants() != null) p.getVariants().size();
        });
        return productPage;
    }

    @Transactional(readOnly = true)
    public Product findBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getVariants() != null) product.getVariants().size();
        return product;
    }

    @Transactional(readOnly = true)
    public List<Product> findByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryCategoryId(categoryId);
        products.forEach(p -> {
            if (p.getVariants() != null) p.getVariants().size();
        });
        return products;
    }

    @Transactional
    public Product create(Product product) {
        if (product.getSlug() == null || product.getSlug().isEmpty()) {
            product.setSlug(toSlug(product.getProName()));
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product update(String slug, Product productDto) {
        Product product = findBySlug(slug);
        product.setProName(productDto.getProName());
        product.setSlug(toSlug(productDto.getProName()));
        product.setDescription(productDto.getDescription());
        product.setMainImage(productDto.getMainImage());
        product.setImages(productDto.getImages());
        product.setCategory(productDto.getCategory());
        product.setBrand(productDto.getBrand());
        product.setActive(productDto.isActive());
        return productRepository.save(product);
    }

    @Transactional
    public void remove(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public Product changeStatus(Long id, boolean isActive) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(isActive);
        return productRepository.save(product);
    }

    private String toSlug(String input) {
        if (input == null) return "";
        String nowhitespace = Pattern.compile("\\s+").matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\w-]").matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
