package com.effectivemobile.socialMedia.service;

import com.effectivemobile.socialMedia.entity.Friendship;
import com.effectivemobile.socialMedia.entity.RelationshipCompositeId;
import com.effectivemobile.socialMedia.entity.Subscription;
import com.effectivemobile.socialMedia.entity.User;
import com.effectivemobile.socialMedia.exception.RelationAlreadyExistsException;
import com.effectivemobile.socialMedia.exception.SubscriptionNotFoundException;
import com.effectivemobile.socialMedia.payload.response.MessageResponse;
import com.effectivemobile.socialMedia.repository.FriendshipRepository;
import com.effectivemobile.socialMedia.repository.SubscriptionRepository;
import com.effectivemobile.socialMedia.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
public class RelationshipService {

    private final SubscriptionRepository subscriptionRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public RelationshipService(SubscriptionRepository subscriptionRepository,
                               FriendshipRepository friendshipRepository, UserRepository userRepository) {

        this.subscriptionRepository = subscriptionRepository;
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Subscription> sendFriendRequest(UUID targetUserId, Principal principal) {
        User currentUser = getCurrentUser(principal);

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        RelationshipCompositeId compositeId = new RelationshipCompositeId(currentUser, targetUser);
        // проверка существует ли такая запись, бд в любом случае не пропустит такое из-за композитного ключа
        if (subscriptionRepository.existsById(compositeId) ||
                subscriptionRepository.existsById(new RelationshipCompositeId(targetUser, currentUser))) {

            throw new RelationAlreadyExistsException("The request has already been sent or the users are already friends");
        }
        Subscription subscription = new Subscription(compositeId, false);
        return new ResponseEntity<>(subscriptionRepository.save(subscription), HttpStatus.CREATED);
    }

    public ResponseEntity<MessageResponse> acceptFriendRequest(UUID subscriberId, Principal principal) {
        User currentUser = getCurrentUser(principal);

        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + subscriberId));

        RelationshipCompositeId compositeId = new RelationshipCompositeId(subscriber, currentUser);
        Subscription subscription = subscriptionRepository.findById(compositeId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Friendship request not found"));

        if (friendshipRepository.existsById(compositeId)) {
            throw new RelationAlreadyExistsException("The users are already friends");
        }

        // если заяка в друзья принята, создаем новую запись в Friendship, а также помечаем accept = true
        subscription.setAccepted(true);
        Friendship friendship = new Friendship(compositeId);
        subscriptionRepository.save(subscription);
        friendshipRepository.save(friendship);

        return new ResponseEntity<>(new MessageResponse("The friendship request was accepted"), HttpStatus.CREATED);
    }

    public ResponseEntity<MessageResponse> deleteFriend(UUID friendId, Principal principal) {
        User currentUser = getCurrentUser(principal);

        User friendUser = userRepository.findById(friendId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with an id: " + friendId));

        // так как нельзя передать композитный id в эндпоинте, то приходится перебрать ключ
        RelationshipCompositeId compositeId = new RelationshipCompositeId(currentUser, friendUser);
        RelationshipCompositeId compositeId2 = new RelationshipCompositeId(friendUser, currentUser);

        if (friendshipRepository.existsById(compositeId)) {
            friendshipRepository.deleteById(compositeId);
            subscriptionRepository.deleteById(compositeId);
            // в этом случае удаляется запись в friendship, subscription и создается новая в subscription
            // так как инициатор удаления друга все равно останется в подписчиках у пользователя
            // поэтому необходимо создать новую запись, в который подписчиком будет человек, которого удалили из друзей
            Subscription subscription = new Subscription(compositeId2, false);
            subscriptionRepository.save(subscription);
        } else if (friendshipRepository.existsById(compositeId2)) {
            friendshipRepository.deleteById(compositeId2);
            Subscription subscription = subscriptionRepository
                    .findById(compositeId2).get();
            subscription.setAccepted(false);
            subscriptionRepository.save(subscription);
        } else {
            throw new SubscriptionNotFoundException("Friendship not found");
        }

        return new ResponseEntity<>(new MessageResponse("A friend has been deleted"), HttpStatus.OK);
    }

    private User getCurrentUser(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }
}
