package com.effectivemobile.socialMedia.controller;

import com.effectivemobile.socialMedia.dto.PostDTO;
import com.effectivemobile.socialMedia.entity.Post;
import com.effectivemobile.socialMedia.payload.response.MessageResponse;
import com.effectivemobile.socialMedia.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Post", description = "API management for posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получить все посты для текущего авторизованного пользователя")
    @GetMapping("/user/all")
    public ResponseEntity<List<Post>> getAllPostsForUser(Principal principal) {
        return postService.getAllPostsForUser(principal);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Создание поста")
    @PostMapping("/create")
    public ResponseEntity<Post> addPost(@Valid @RequestBody PostDTO post, Principal principal) {
        return postService.createPost(post, principal);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновление поста")
    @PatchMapping("/update/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable("postId") UUID postId, @Valid @RequestBody PostDTO post) {
        return postService.updatePost(post, postId);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление поста")
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<MessageResponse> delete(@PathVariable("postId") UUID postId) {
        return postService.deletePost(postId);
    }
}
