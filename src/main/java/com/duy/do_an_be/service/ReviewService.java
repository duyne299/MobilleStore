package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.Review;
import com.duy.do_an_be.repository.ReviewRepository;
import com.duy.do_an_be.repository.ProductRepository;
import com.duy.do_an_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final com.duy.do_an_be.repository.OrderRepository orderRepository;

    public Page<Review> findAll(String search, int page, int limit) {
        if (search != null && !search.isEmpty()) {
            return reviewRepository.findByContentContainingIgnoreCase(search, PageRequest.of(page - 1, limit));
        }
        return reviewRepository.findAll(PageRequest.of(page - 1, limit));
    }

    public List<Review> findByProductId(Long proId) {
        return reviewRepository.findByProductProId(proId);
    }

    public Review findOne(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Transactional
    public Review create(Review review, Long proId, String comment) {
        if (comment != null && !comment.trim().isEmpty()) {
            review.setContent(comment);
        }

        if (proId != null) {
            productRepository.findById(proId).ifPresent(review::setProduct);
        }

        // Resolve user from current authenticated security context
        com.duy.do_an_be.entity.User currentUser = null;
        try {
            org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                String email = auth.getName();
                currentUser = userRepository.findByEmail(email).orElse(null);
                review.setUser(currentUser);
            }
        } catch (Exception e) {
            // Ignore
        }

        // Securing review creation: if user rates the product (rating > 0), they must have purchased it
        if (review.getRating() != null && review.getRating() > 0) {
            if (currentUser == null || proId == null || !orderRepository.existsByUserIdAndProIdAndNotCancelled(currentUser.getUserId(), proId)) {
                throw new RuntimeException("Bạn cần mua sản phẩm này trước khi gửi đánh giá!");
            }
        }

        return reviewRepository.save(review);
    }

    @Transactional
    public Review update(Long id, Review reviewDto) {
        Review review = findOne(id);
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        review.setActive(reviewDto.isActive());
        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateStatus(Long id, boolean isActive) {
        Review review = findOne(id);
        review.setActive(isActive);
        return reviewRepository.save(review);
    }

    @Transactional
    public void remove(Long id) {
        reviewRepository.deleteById(id);
    }

    public java.util.Map<String, Object> getRating(Long proId) {
        Double avgRating = reviewRepository.calculateAverageRatingByProId(proId);
        Long totalReviews = reviewRepository.countByProductProIdAndRatingGreaterThan(proId, 0);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("proId", proId);
        result.put("avgRating", avgRating != null ? avgRating : 0.0);
        result.put("totalReviews", totalReviews);
        return result;
    }

    public boolean checkPurchase(Long proId) {
        try {
            org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                String email = auth.getName();
                java.util.Optional<com.duy.do_an_be.entity.User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                    return orderRepository.existsByUserIdAndProIdAndNotCancelled(userOpt.get().getUserId(), proId);
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return false; 
    }
}
