package com.duy.do_an_be.controller;

import com.duy.do_an_be.dto.request.PostUpdateRequest;
import com.duy.do_an_be.entity.Post;
import com.duy.do_an_be.repository.PostRepository;
import com.duy.do_an_be.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final FileService fileService;
    private final com.duy.do_an_be.service.PostService postService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<Post> postPage;
        if (search != null && !search.isEmpty()) {
            postPage = postRepository.findByTitleContainingIgnoreCase(search, PageRequest.of(page - 1, limit));
        } else {
            postPage = postRepository.findAll(PageRequest.of(page - 1, limit));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data", postPage.getContent());
        response.put("total", postPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Post> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postRepository.findBySlug(slug).orElseThrow());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Post> create(
            @ModelAttribute Post post,
            @RequestParam(value = "image", required = false) MultipartFile thumbnail,
            @RequestParam(value = "authorId", required = false) Long authorId
    ) {
        if (thumbnail != null && !thumbnail.isEmpty()) {
            post.setThumbnail(fileService.saveFile(thumbnail));
        }
        return ResponseEntity.ok(postService.create(post, authorId));
    }

    @PutMapping(value = "/{slug}", consumes = {"multipart/form-data"})
    public ResponseEntity<Post> update(
            @PathVariable String slug,
            @ModelAttribute PostUpdateRequest postDto,
            @RequestParam(value = "image", required = false) MultipartFile thumbnail,
            @RequestParam(value = "authorId", required = false) Long authorId
    ) {
        if (thumbnail != null && !thumbnail.isEmpty()) {
            postDto.setThumbnail(fileService.saveFile(thumbnail));
        }
        return ResponseEntity.ok(postService.update(slug, postDto, authorId));
    }

    @PutMapping(value = "/{slug}", consumes = {"application/json"})
    public ResponseEntity<Post> updateJson(
            @PathVariable String slug,
            @RequestBody PostUpdateRequest postDto,
            @RequestParam(value = "authorId", required = false) Long authorId
    ) {
        return ResponseEntity.ok(postService.update(slug, postDto, authorId));
    }

    @PutMapping(value = "/{id:\\d+}", consumes = {"multipart/form-data"})
    public ResponseEntity<Post> updateById(
            @PathVariable Long id,
            @ModelAttribute PostUpdateRequest postDto,
            @RequestParam(value = "image", required = false) MultipartFile thumbnail,
            @RequestParam(value = "authorId", required = false) Long authorId
    ) {
        if (thumbnail != null && !thumbnail.isEmpty()) {
            postDto.setThumbnail(fileService.saveFile(thumbnail));
        }
        return ResponseEntity.ok(postService.updateById(id, postDto, authorId));
    }

    @PutMapping(value = "/{id:\\d+}", consumes = {"application/json"})
    public ResponseEntity<Post> updateByIdJson(
            @PathVariable Long id,
            @RequestBody PostUpdateRequest postDto,
            @RequestParam(value = "authorId", required = false) Long authorId
    ) {
        return ResponseEntity.ok(postService.updateById(id, postDto, authorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Post> changeStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setActive(body.get("isActive"));
        return ResponseEntity.ok(postRepository.save(post));
    }
}
