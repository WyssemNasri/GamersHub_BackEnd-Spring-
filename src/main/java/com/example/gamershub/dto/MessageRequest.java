package com.example.gamershub.dto;

import lombok.Data;

// Classe utilisée pour recevoir les données JSON envoyées par le client
@Data
public class MessageRequest {
    private Long senderId;
    private Long receiverId;
    private String content;
}
