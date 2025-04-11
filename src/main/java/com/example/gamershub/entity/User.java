package com.example.gamershub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;

import com.example.gamershub.dto.FriendDTO;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", unique = true, length = 50)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "phoneNumber", unique = true)
    private String phoneNumber;

    @Column(name = "dayOfBirth")
    private Date dayOfBirth;

    @Column(name = "Token")
    private String token;

    @Column(name = "profilePicture", columnDefinition = "TEXT") 
    private String profilePicture; 

    @Column(name = "coverPicture", columnDefinition = "TEXT") 
    private String coverPicture; 

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    public User(long id, String email, String password, String firstName, String lastName, 
                String phoneNumber, Date dayOfBirth, String token, String profilePicture, 
                String coverPicture, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dayOfBirth = dayOfBirth;
        this.token = token;
        this.profilePicture = profilePicture;
        this.coverPicture = coverPicture;
        this.createdAt = createdAt;
    }

    public FriendDTO toFriendDTO(User user){

        FriendDTO frienddto = new FriendDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getProfilePicture()) ; 
        return frienddto  ;
    }


    

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
