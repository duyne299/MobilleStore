package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.Brand;
import com.duy.do_an_be.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public Page<Brand> findAll(String search, int page, int limit) {
        if (search != null && !search.isEmpty()) {
            return brandRepository.findByBrandNameContainingIgnoreCase(search, PageRequest.of(page - 1, limit));
        }
        return brandRepository.findAll(PageRequest.of(page - 1, limit));
    }

    public Brand findOne(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
    }

    @Transactional
    public Brand create(Brand brand) {
        if (brandRepository.existsByBrandName(brand.getBrandName())) {
            throw new RuntimeException("Brand name already exists");
        }
        if (brand.getSlug() == null || brand.getSlug().isEmpty()) {
            brand.setSlug(toSlug(brand.getBrandName()));
        }
        return brandRepository.save(brand);
    }

    @Transactional
    public Brand update(Long id, Brand brandDto) {
        Brand brand = findOne(id);
        brand.setBrandName(brandDto.getBrandName());
        brand.setSlug(toSlug(brandDto.getBrandName()));
        brand.setBrandLogo(brandDto.getBrandLogo());
        brand.setActive(brandDto.isActive());
        return brandRepository.save(brand);
    }

    @Transactional
    public void remove(Long id) {
        brandRepository.deleteById(id);
    }

    @Transactional
    public Brand updateStatus(String slug, boolean isActive) {
        Brand brand = brandRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        brand.setActive(isActive);
        return brandRepository.save(brand);
    }

    public boolean checkName(String name) {
        return brandRepository.existsByBrandName(name);
    }

    private String toSlug(String input) {
        if (input == null) return "";
        String nowhitespace = Pattern.compile("\\s+").matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\w-]").matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
