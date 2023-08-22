package com.effectivemobile.socialMedia.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "Встраиваемая сущность, определяющая композитный id для достижения уникальности записи с двумя внешними ключами.")
@Embeddable
public class RelationshipCompositeId implements Serializable {

    @Serial
    private static final long serialVersionUID = 7083813070908579977L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_user_id")
    private User relatedUser;

    private RelationshipCompositeId() {
    }

    public RelationshipCompositeId(User user, User relatedUser) {
        this.user = user;
        this.relatedUser = relatedUser;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RelationshipCompositeId relationshipCompositeId = (RelationshipCompositeId) obj;
        if (!user.equals(relationshipCompositeId.user)) {
            return false;
        }
        return relatedUser.equals(relationshipCompositeId.relatedUser);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + relatedUser.hashCode();
        return result;
    }

    public User getUser() {
        return user;
    }

    public User getRelatedUser() {
        return relatedUser;
    }
}
