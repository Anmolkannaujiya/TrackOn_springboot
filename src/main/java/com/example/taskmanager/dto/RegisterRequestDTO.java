package com.example.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

    @NotBlank(message = "E-mail is required")
    @Email(message = "Invalid E-mail format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=6,max=50, message = "Password must be at least 6 character long")
    private String password;

    @NotBlank(message = "username is required")
    @Size(min = 2, max =100, message ="Username must be between 2 and 100 characters")
    private String username;

    //getters and setters
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
