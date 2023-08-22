package com.effectivemobile.socialMedia.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Schema(description = "Сущность, представляющая дружбу между пользователями")
@Entity
public class Friendship {

    @Schema(description = "Композитный id для формирования уникальности сочетания двух внешних ключей")
    @EmbeddedId
    private RelationshipCompositeId id;

    public Friendship() {
    }

    public Friendship(RelationshipCompositeId id) {
        this.id = id;
    }

    public RelationshipCompositeId getId() {
        return id;
    }

    public void setId(RelationshipCompositeId id) {
        this.id = id;
    }
}
