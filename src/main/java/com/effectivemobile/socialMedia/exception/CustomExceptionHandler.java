package com.effectivemobile.socialMedia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> map = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return map;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFoundException.class)
    public Map<String, String> handlePostNotFoundException(PostNotFoundException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public Map<String, String> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SubscriptionNotFoundException.class)
    public Map<String, String> handleSubscriptionNotFoundException(SubscriptionNotFoundException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(RelationAlreadyExistsException.class)
    public Map<String, String> handleRelationAlreadyExistsException(RelationAlreadyExistsException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public Map<String, String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("errorMessage", exception.getMessage());
        return map;
    }
}