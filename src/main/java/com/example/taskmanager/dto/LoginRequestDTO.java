package com.example.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
    @NotBlank(message = "E-mail is required")
    @Email(message = "Enter a valid E-mail")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    //getter and setter
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
