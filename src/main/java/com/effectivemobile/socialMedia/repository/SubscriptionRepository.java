package com.effectivemobile.socialMedia.repository;

import com.effectivemobile.socialMedia.entity.RelationshipCompositeId;
import com.effectivemobile.socialMedia.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, RelationshipCompositeId> {

}
