package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.ProductVariant;
import com.duy.do_an_be.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    public List<ProductVariant> findAll() {
        return productVariantRepository.findAll();
    }

    public ProductVariant findOne(Long id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant not found"));
    }

    public List<ProductVariant> findByProductId(Long proId) {
        return productVariantRepository.findByProductProId(proId);
    }

    @Transactional
    public ProductVariant create(ProductVariant variant) {
        return productVariantRepository.save(variant);
    }

    @Transactional
    public ProductVariant update(Long id, ProductVariant variantDto) {
        ProductVariant variant = findOne(id);
        variant.setRom(variantDto.getRom());
        variant.setColor(variantDto.getColor());
        variant.setExtraPrice(variantDto.getExtraPrice());
        variant.setActive(variantDto.isActive());
        
        variant.setWarehouseId(variantDto.getWarehouseId());
        variant.setQuantity(variantDto.getQuantity());
        variant.setImportPrice(variantDto.getImportPrice());
        variant.setBaseSalePrice(variantDto.getBaseSalePrice());
        
        return productVariantRepository.save(variant);
    }

    @Transactional
    public ProductVariant updateStatus(Long id, boolean isActive) {
        ProductVariant variant = findOne(id);
        variant.setActive(isActive);
        return productVariantRepository.save(variant);
    }

    @Transactional
    public void remove(Long id) {
        productVariantRepository.deleteById(id);
    }

    @Transactional
    public ProductVariant updateStock(Long id, Integer quantityAdjustment) {
        ProductVariant variant = findOne(id);
        int newQuantity = (variant.getQuantity() != null ? variant.getQuantity() : 0) + quantityAdjustment;
        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock for variant ID: " + id);
        }
        variant.setQuantity(newQuantity);
        return productVariantRepository.save(variant);
    }
}
