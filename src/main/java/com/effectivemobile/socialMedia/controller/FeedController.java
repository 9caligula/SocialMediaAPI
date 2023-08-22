package com.effectivemobile.socialMedia.controller;

import com.effectivemobile.socialMedia.entity.Post;
import com.effectivemobile.socialMedia.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "Feed", description = "API management for the news feed")
public class FeedController {

    private final PostService postService;

    public FeedController(PostService postService) {
        this.postService = postService;
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получить ленту новостей с постами на подписанных пользователей")
    @GetMapping("/feed")
    public ResponseEntity<Page<Post>> getUserFeed(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  Principal principal) {

        return postService.getUserFeed(page, size, principal);
    }
}
