package com.example.gamershub.dto;

public class FriendDTO {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String profilePicture;

    public FriendDTO(Long id, String firstName, String lastName, String profilePicture) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
