package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.Banner;
import com.duy.do_an_be.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    public Page<Banner> findAll(String search, int page, int limit) {
        if (search != null && !search.isEmpty()) {
            return bannerRepository.findByTitleContainingIgnoreCase(search, PageRequest.of(page - 1, limit));
        }
        return bannerRepository.findAll(PageRequest.of(page - 1, limit));
    }

    public Banner findOne(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found"));
    }

    @Transactional
    public Banner create(Banner banner) {
        return bannerRepository.save(banner);
    }

    @Transactional
    public Banner update(Long id, Banner bannerDto) {
        Banner banner = findOne(id);
        if (bannerDto.getTitle() != null && !bannerDto.getTitle().trim().isEmpty()) {
            banner.setTitle(bannerDto.getTitle());
        }
        if (bannerDto.getImageUrl() != null && !bannerDto.getImageUrl().trim().isEmpty()) {
            banner.setImageUrl(bannerDto.getImageUrl());
        }
        if (bannerDto.getExcerpt() != null) {
            banner.setExcerpt(bannerDto.getExcerpt());
        }
        if (bannerDto.getLinkTarget() != null && !bannerDto.getLinkTarget().trim().isEmpty()) {
            banner.setLinkTarget(bannerDto.getLinkTarget());
        }
        if (bannerDto.getPosition() != null) {
            banner.setPosition(bannerDto.getPosition());
        }
        banner.setActive(bannerDto.isActive());
        if (bannerDto.getStartDate() != null) {
            banner.setStartDate(bannerDto.getStartDate());
        }
        if (bannerDto.getEndDate() != null) {
            banner.setEndDate(bannerDto.getEndDate());
        }
        return bannerRepository.save(banner);
    }

    @Transactional
    public void remove(Long id) {
        bannerRepository.deleteById(id);
    }

    @Transactional
    public Banner updateStatus(Long id, boolean isActive) {
        Banner banner = findOne(id);
        banner.setActive(isActive);
        return bannerRepository.save(banner);
    }
}
