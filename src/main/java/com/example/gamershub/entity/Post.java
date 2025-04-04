package com.example.gamershub.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.example.gamershub.dto.FriendDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) 
    @JsonIgnore
    private User user ; 

    @Column(name = "description", nullable = false)
    private String description; 

    @Column(name = "url_media")
    private String urlMedia; 

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Post() {}

    public Post(String description, String urlMedia) {
        this.description = description;
        this.urlMedia = urlMedia;

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user; // Return the User entity
    }

    public void setUser(User user) { // Accept User entity
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlMedia() {
        return urlMedia;
    }

    public void setUrlMedia(String urlMedia) {
        this.urlMedia = urlMedia;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public FriendDTO getUserDTO() {
        if (this.user != null) {
            return new FriendDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getProfilePicture());
        }
        return null;
    }
 
}
