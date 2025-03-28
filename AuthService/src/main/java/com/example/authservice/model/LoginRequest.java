package com.example.authservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class LoginRequest {

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

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
