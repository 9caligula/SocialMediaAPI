package com.effectivemobile.socialMedia.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username should not be null")
    @Size(min = 3, message = "The username must have more than 2 characters")
    private String username;

    @Email
    @NotBlank(message = "Email should not be null")
    private String email;

    @NotBlank(message = "Password should not be null")
    @Size(min = 3, message = "The password must have more than 2 characters")
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}