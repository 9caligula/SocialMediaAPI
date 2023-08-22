package com.effectivemobile.socialMedia.controller;

import com.effectivemobile.socialMedia.entity.Subscription;
import com.effectivemobile.socialMedia.payload.response.MessageResponse;
import com.effectivemobile.socialMedia.service.RelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Relationship", description = "API that manages the lifecycle of relationships between users")
public class RelationshipController {

    private final RelationshipService relationshipService;

    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Подписка на пользователя по его id и отправка запроса на дружбу")
    @PostMapping("/subscribe/{targetUserId}")
    public ResponseEntity<Subscription> sendFriendRequest(@PathVariable("targetUserId") UUID targetUserId,
                                                          Principal principal) {
        return relationshipService.sendFriendRequest(targetUserId, principal);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Принятие запроса на дружбу")
    @PostMapping("/accept-friend-request/{subscriberId}")
    public ResponseEntity<MessageResponse> acceptFriendRequest(@PathVariable("subscriberId") UUID subscriberId,
                                                               Principal principal) {
        return relationshipService.acceptFriendRequest(subscriberId, principal);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление друга")
    @PostMapping("/friend/delete/{friendId}")
    public ResponseEntity<MessageResponse> deleteFriend(@PathVariable("friendId") UUID friendId, Principal principal) {
        return relationshipService.deleteFriend(friendId, principal);
    }
}
