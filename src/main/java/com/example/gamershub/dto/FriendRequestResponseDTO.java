package com.example.gamershub.dto;

public class FriendRequestResponseDTO {
    private Long requestId;
    private FriendDTO user;
    private String status;

    public FriendRequestResponseDTO(Long requestId, FriendDTO user, String status) {
        this.requestId = requestId;
        this.user = user;
        this.status = status;
    }

    public Long getRequestId() {
        return requestId;
    }

    public FriendDTO getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }
}
