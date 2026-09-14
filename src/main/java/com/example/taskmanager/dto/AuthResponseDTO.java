package com.example.taskmanager.dto;

public class AuthResponseDTO {
    private String token;
    private String tokenType;
    private Long expiresIn;

    //constructor of Authresponse for jwt token
    public AuthResponseDTO(String token,
                           String tokenType,
                           Long expiresIn){
        this.token = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    //only getters no setters is needed
    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
