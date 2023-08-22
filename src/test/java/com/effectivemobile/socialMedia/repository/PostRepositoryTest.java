package com.effectivemobile.socialMedia.repository;

import com.effectivemobile.socialMedia.entity.Post;
import com.effectivemobile.socialMedia.entity.RelationshipCompositeId;
import com.effectivemobile.socialMedia.entity.Subscription;
import com.effectivemobile.socialMedia.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    void itShouldFindAllPostByUserId() {
        // given
        User user = new User("testuser", "test@example.com", "password");
        Post post1 = new Post(user, "Title 1", "Content 1");
        Post post2 = new Post(user, "Title 2", "Content 2");
        user.getPosts().add(post1);
        user.getPosts().add(post2);
        userRepository.save(user);
        postRepository.save(post1);
        postRepository.save(post2);

        // when
        List<Post> foundPosts = postRepository.findAllByUserId(user.getId());

        // then
        assertThat(foundPosts).hasSize(2);
        assertThat(foundPosts).extracting(Post::getUser).containsOnly(user);
    }

    @Test
    void itShouldFindPostsFromSubscribedUsers() {
        // given
        User currentUser = new User("currentUser", "current@example.com", "password");
        User subscribedUser = new User("subscribedUser", "subscribed@example.com", "password");

        Post post1 = new Post(currentUser, "Title 1", "Content 1");
        Post post2 = new Post(subscribedUser, "Title 2", "Content 2");

        currentUser.getPosts().add(post1);
        subscribedUser.getPosts().add(post2);
        userRepository.save(currentUser);
        userRepository.save(subscribedUser);
        postRepository.save(post1);
        postRepository.save(post2);

        RelationshipCompositeId compositeId = new RelationshipCompositeId(currentUser, subscribedUser);
        Subscription subscription = new Subscription(compositeId, false);
        subscriptionRepository.save(subscription);

        // when
        Page<Post> page = postRepository.findPostsFromSubscribedUsers(currentUser.getId(), PageRequest.of(0, 10));

        // then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getUser()).isEqualTo(subscribedUser);
    }
}