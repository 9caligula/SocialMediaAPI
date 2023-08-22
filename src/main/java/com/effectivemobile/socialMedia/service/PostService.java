package com.effectivemobile.socialMedia.service;

import com.effectivemobile.socialMedia.dto.PostDTO;
import com.effectivemobile.socialMedia.entity.Post;
import com.effectivemobile.socialMedia.entity.User;
import com.effectivemobile.socialMedia.exception.PostNotFoundException;
import com.effectivemobile.socialMedia.payload.response.MessageResponse;
import com.effectivemobile.socialMedia.repository.PostRepository;
import com.effectivemobile.socialMedia.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<Post>> getAllPostsForUser(Principal principal) {
        User currentUser = getCurrentUser(principal);
        List<Post> posts = postRepository.findAllByUserId(currentUser.getId());
        if (posts.isEmpty()) {
            throw new PostNotFoundException("No posts found for a user with a username: " + currentUser.getUsername());
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    public ResponseEntity<Post> createPost(PostDTO postDTO, Principal principal) {
        // Нет надобности валидировать данные, так как DTO и Controller помечены соответствующими аннотациями
        User currentUser = getCurrentUser(principal);
        Post createdPost = new Post(currentUser, postDTO.getTitle(), postDTO.getContent());
        return new ResponseEntity<>(postRepository.save(createdPost), HttpStatus.CREATED);
    }

    public ResponseEntity<Post> updatePost(PostDTO postDTO, UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        post.setContent(postDTO.getContent());
        post.setTitle(postDTO.getTitle());
        return new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponse> deletePost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found with id: " + postId);
        }
        postRepository.deleteById(postId);
        return new ResponseEntity<>(new MessageResponse("Post was successful deleted"), HttpStatus.OK);
    }

    public ResponseEntity<Page<Post>> getUserFeed(int page, int size, Principal principal) {
        User currentUser = getCurrentUser(principal);

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> userFeed = postRepository.findPostsFromSubscribedUsers(currentUser.getId(), pageable);

        return new ResponseEntity<>(userFeed, HttpStatus.OK);
    }

    private User getCurrentUser(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }
}
