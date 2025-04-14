package com.example.gamershub.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String type;
    private String message;
    private Long senderId;
    private Long receiverId;
    private String senderUsername;
private String receiverUsername;

// Getter & Setter pour senderUsername
public String getSenderUsername() {
    return senderUsername;
}

public void setSenderUsername(String senderUsername) {
    this.senderUsername = senderUsername;
}

// Getter & Setter pour receiverUsername
public String getReceiverUsername() {
    return receiverUsername;
}

public void setReceiverUsername(String receiverUsername) {
    this.receiverUsername = receiverUsername;
}

}
