package com.example.gamershub.entity;

import com.example.gamershub.dto.FriendDTO;
import com.example.gamershub.dto.FriendRequestResponseDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FriendRequest {
    public enum RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
    public FriendRequest() {}

    public FriendRequest(User sender, User receiver, RequestStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }
    public FriendDTO recevFriendDTO (){
            FriendDTO frienddto = new FriendDTO(receiver.getId(), receiver.getFirstName(), receiver.getLastName(), receiver.getProfilePicture()) ; 
            return frienddto  ;
        }
    public FriendRequestResponseDTO toSentDTO() {
    FriendDTO receiverDTO = new FriendDTO(receiver.getId(), receiver.getFirstName(), receiver.getLastName(), receiver.getProfilePicture());
    return new FriendRequestResponseDTO(id, receiverDTO, status.name());
}

public FriendRequestResponseDTO toReceivedDTO() {
    FriendDTO senderDTO = new FriendDTO(sender.getId(), sender.getFirstName(), sender.getLastName(), sender.getProfilePicture());
    return new FriendRequestResponseDTO(id, senderDTO, status.name());
}


    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    
}
