package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.Category;
import com.duy.do_an_be.repository.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Page<Category> findAll(String search, int page, int limit) {
        if (search != null && !search.isEmpty()) {
            return categoryRepository.findByCategoryNameContainingIgnoreCase(search, PageRequest.of(page - 1, limit));
        }
        return categoryRepository.findAll(PageRequest.of(page - 1, limit));
    }

    public Category findOne(String idOrSlug) {
        try {
            Long id = Long.parseLong(idOrSlug);
            return categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        } catch (NumberFormatException e) {
            return categoryRepository.findBySlug(idOrSlug)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }
    }

    @Transactional
    public Category create(Category category) {
        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new RuntimeException("Category name already exists");
        }
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(toSlug(category.getCategoryName()));
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(String slug, Category categoryDto) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setCategoryName(categoryDto.getCategoryName());
        category.setSlug(toSlug(categoryDto.getCategoryName()));
        category.setDescription(categoryDto.getDescription());
        category.setParent(categoryDto.getParent());
        category.setActive(categoryDto.isActive());
        // Chỉ cập nhật ảnh nếu trong DTO có giá trị (gửi từ frontend) 
        // hoặc nếu giá trị mới là null (người dùng chủ động xóa)
        // Lưu ý: Controller đã xử lý việc lưu file và set vào DTO trước khi gọi service này
        category.setCategoryImage(categoryDto.getCategoryImage());
        
        return categoryRepository.save(category);
    }

    @Transactional
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public Category updateStatus(String slug, boolean isActive) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setActive(isActive);
        return categoryRepository.save(category);
    }

    public boolean checkName(String name) {
        return categoryRepository.existsByCategoryName(name);
    }

    private String toSlug(String input) {
        if (input == null) return "";
        
        String slug = input.toLowerCase(Locale.ENGLISH);
        slug = slug.replaceAll("[áàảãạăắằẳẵặâấầẩẫậ]", "a");
        slug = slug.replaceAll("[éèẻẽẹêếềểễệ]", "e");
        slug = slug.replaceAll("[íìỉĩị]", "i");
        slug = slug.replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o");
        slug = slug.replaceAll("[úùủũụưứừửữự]", "u");
        slug = slug.replaceAll("[ýỳỷỹỵ]", "y");
        slug = slug.replaceAll("[đ]", "d");
        
        slug = Pattern.compile("\\s+").matcher(slug).replaceAll("-");
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = Pattern.compile("[^\\w-]").matcher(slug).replaceAll("");
        return slug;
    }
}
