package com.effectivemobile.socialMedia.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Schema(description = "Сущность, представляющая подписку пользователя на другого пользователя")
@Entity
public class Subscription {

    @EmbeddedId
    private RelationshipCompositeId id;

    @Schema(description = "Указывает на то, принята ли заявка в друзья")
    @Column(nullable = false)
    private boolean accepted;

    public Subscription() {
    }

    public Subscription(RelationshipCompositeId id, boolean accepted) {
        this.id = id;
        this.accepted = accepted;
    }

    public RelationshipCompositeId getId() {
        return id;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
