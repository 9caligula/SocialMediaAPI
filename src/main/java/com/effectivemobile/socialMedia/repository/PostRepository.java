package com.effectivemobile.socialMedia.repository;

import com.effectivemobile.socialMedia.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query(value = "SELECT e from Post e where e.user.id = :userId")
    List<Post> findAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT p FROM Post p " +
            "WHERE p.user.id IN (SELECT s.id.relatedUser.id FROM Subscription s WHERE s.id.user.id = :currentUser) " +
            "ORDER BY p.createdOn DESC")
    Page<Post> findPostsFromSubscribedUsers(@Param("currentUser") UUID currentUser, Pageable pageable);
}
