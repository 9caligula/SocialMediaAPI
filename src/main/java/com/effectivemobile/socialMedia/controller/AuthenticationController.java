package com.effectivemobile.socialMedia.controller;

import com.effectivemobile.socialMedia.entity.User;
import com.effectivemobile.socialMedia.payload.request.AuthenticationRequest;
import com.effectivemobile.socialMedia.payload.request.RegisterRequest;
import com.effectivemobile.socialMedia.payload.response.AuthenticationResponse;
import com.effectivemobile.socialMedia.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication management API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService service) {
        this.authenticationService = service;
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.OK);
    }

    @Operation(summary = "Аутентификация пользователя",
            description = "После успешной аутентификации выдается JWT Bearer token.")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получить всех зарегистрированных пользователей",
            description = "Разумеется доступен исключительно для тестов и должен использоваться пользователями с ролью admin. " +
                    "Но так как в задании не было оговорено создание ролей, то эндпоинт имеет место быть."
    )
    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return authenticationService.getAllUsers();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получить пользователя по username")
    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return authenticationService.getUserByUsername(username);
    }
}