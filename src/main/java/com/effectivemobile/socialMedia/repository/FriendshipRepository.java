package com.effectivemobile.socialMedia.repository;

import com.effectivemobile.socialMedia.entity.Friendship;
import com.effectivemobile.socialMedia.entity.RelationshipCompositeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, RelationshipCompositeId> {
}
