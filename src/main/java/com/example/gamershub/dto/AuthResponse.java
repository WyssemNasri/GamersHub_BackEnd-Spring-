package com.example.gamershub.dto;

public class AuthResponse {
    
    private String token;
    private String userId;  

    public AuthResponse(String token, String userId) {
        this.token = token;
        this.userId = userId;  
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId; 
    }
}
