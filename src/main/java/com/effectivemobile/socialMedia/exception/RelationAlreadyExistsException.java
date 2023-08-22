package com.effectivemobile.socialMedia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RelationAlreadyExistsException extends RuntimeException {
    public RelationAlreadyExistsException(String message) {
        super(message);
    }
}
