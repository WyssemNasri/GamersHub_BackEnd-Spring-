package com.example.gamershub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Utilisateur qui envoie le message
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    // Utilisateur qui reçoit le message
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String content; // Contenu du message

    private LocalDateTime createdAt; // Date d'envoi

    // Initialiser createdAt automatiquement
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
