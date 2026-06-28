package com.duy.do_an_be.service;

import com.duy.do_an_be.dto.request.PostUpdateRequest;
import com.duy.do_an_be.entity.Post;
import com.duy.do_an_be.entity.User;
import com.duy.do_an_be.repository.PostRepository;
import com.duy.do_an_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post create(Post post, Long authorId) {
        if (post.getSlug() == null || post.getSlug().isEmpty()) {
            post.setSlug(toSlug(post.getTitle()));
        }
        
        if (authorId != null && authorId > 0) {
            userRepository.findById(authorId).ifPresent(post::setAuthor);
        }
        
        return postRepository.save(post);
    }

    @Transactional
    public Post update(String slug, PostUpdateRequest postDto, Long authorId) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        applyPostUpdate(post, postDto, authorId);
        return postRepository.save(post);
    }

    @Transactional
    public Post updateById(Long postId, PostUpdateRequest postDto, Long authorId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        applyPostUpdate(post, postDto, authorId);
        return postRepository.save(post);
    }

    private void applyPostUpdate(Post post, PostUpdateRequest postDto, Long authorId) {
        if (postDto.getTitle() != null) {
            post.setTitle(postDto.getTitle());
            post.setSlug(toSlug(postDto.getTitle()));
        }
        if (postDto.getContent() != null) {
            post.setContent(postDto.getContent());
        }
        if (postDto.getExcerpt() != null) {
            post.setExcerpt(postDto.getExcerpt());
        }
        if (postDto.getIsActive() != null) {
            post.setActive(postDto.getIsActive());
        }
        if (postDto.getThumbnail() != null) {
            post.setThumbnail(postDto.getThumbnail());
        }

        if (authorId != null && authorId > 0) {
            userRepository.findById(authorId).ifPresent(post::setAuthor);
        }
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
