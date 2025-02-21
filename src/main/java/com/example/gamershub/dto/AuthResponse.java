package com.example.gamershub.dto;

public class AuthResponse {
    
    private String token;
    private String userId;  // Ajoutez un champ pour l'ID de l'utilisateur

    public AuthResponse(String token, String userId) {
        this.token = token;
        this.userId = userId;  // Initialisez l'ID dans le constructeur
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;  // Ajoutez un getter pour l'ID de l'utilisateur
    }
}
