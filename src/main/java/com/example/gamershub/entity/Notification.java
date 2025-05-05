package com.example.gamershub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String message;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "receiver_id",  nullable = false)
    private User receiver;

    private boolean isRead = false;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification(String type , String message , User sender , User receiver){
        this.type=type;
        this.message=message ; 
        this.sender = sender ; 
        this.receiver= receiver ; 
    }
}
