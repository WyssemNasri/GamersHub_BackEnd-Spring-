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
    private User user;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "url_media")
    private String urlMedia;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Like> likes;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Comment> comments;

    @Transient
    private int likeCount;

    @Transient
    private int commentCount;

    @Transient
    private List<FriendDTO> usersWhoLiked;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Post() {}

    public Post(String description, String urlMedia) {
        this.description = description;
        this.urlMedia = urlMedia;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
        this.likeCount = likes != null ? likes.size() : 0; // Set like count based on the number of likes
    }

    public int getLikeCount() {
        return likeCount;  // Get calculated like count
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentCount = comments != null ? comments.size() : 0;  // Set comment count based on the number of comments
    }

    public int getCommentCount() {
        return commentCount;  // Get calculated comment count
    }

    // Method to set like count (fixing the undefined method error)
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    // Method to set users who liked the post (fixing the undefined method error)
    public void setUsersWhoLiked(List<FriendDTO> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked;
    }

    // Method to set UserDTO for the post owner (fixing the undefined method error)
    public void setUserDTO(FriendDTO userDTO) {
        // This is used to set a DTO instead of the actual User entity
        // For instance, you can store userDTO in the post if needed for frontend display
    }

    public FriendDTO getUserDTO() {
        if (this.user != null) {
            return new FriendDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getProfilePicture());
        }
        return null;
    }
}
