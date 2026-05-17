package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.ProductSpecification;
import com.duy.do_an_be.repository.ProductSpecificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSpecificationService {

    private final ProductSpecificationRepository repository;

    public List<ProductSpecification> findAll() {
        return repository.findAll();
    }

    public ProductSpecification findOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specification not found"));
    }

    @Transactional
    public ProductSpecification create(ProductSpecification spec) {
        return repository.save(spec);
    }

    @Transactional
    public ProductSpecification update(Long id, ProductSpecification specDto) {
        ProductSpecification spec = findOne(id);
        spec.setOs(specDto.getOs());
        spec.setDisplay(specDto.getDisplay());
        spec.setCpu(specDto.getCpu());
        spec.setGpu(specDto.getGpu());
        spec.setRam(specDto.getRam());
        spec.setRom(specDto.getRom());
        spec.setCameraFront(specDto.getCameraFront());
        spec.setCameraRear(specDto.getCameraRear());
        spec.setBattery(specDto.getBattery());
        spec.setWeight(specDto.getWeight());
        spec.setSize(specDto.getSize());
        spec.setSim(specDto.getSim());
        spec.setMaterial(specDto.getMaterial());
        return repository.save(spec);
    }

    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }
}
