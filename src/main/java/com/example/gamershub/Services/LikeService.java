package com.example.gamershub.Services;

import com.example.gamershub.Respositroys.LikeRepository;
import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.FriendDTO;
import com.example.gamershub.dto.LikeRequest;
import com.example.gamershub.dto.NotificationDTO;
import com.example.gamershub.entity.Like;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;


    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository , NotificationService notificationService) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.notificationService=notificationService;
    }
   public void likePost(LikeRequest likeRequest) {

    User user = userRepository.findById(likeRequest.getUserid()).orElseThrow(() -> new RuntimeException("User not found"));
    Post post = postRepository.findById(likeRequest.getPostid()).orElseThrow(() -> new RuntimeException("Post not found"));
    Like existingLike = likeRepository.findByUserAndPost(user, post);
    if (existingLike != null) {

        likeRepository.delete(existingLike);
    } else {
    Like like = new Like();
    like.setUser(user);
    like.setPost(post);
    likeRepository.save(like);

    // Notification
    if (user.getId() != post.getUser().getId()){  
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setSenderId(user.getId());
        notificationDTO.setReceiverId(post.getUser().getId());
        notificationDTO.setMessage(user.getFirstName() +user.getLastName()+ " a aimé votre publication.");
        notificationDTO.setType("PostLiked");

        notificationService.saveNotification(notificationDTO);  
        notificationService.sendNotification(notificationDTO);  
    }
}

}
    public void deleteLike(Long likeId) {
        Like like = likeRepository.findById(likeId)
                .orElseThrow(() -> new EntityNotFoundException("Like not found"));
        likeRepository.delete(like);
    }

    public List<Like> getLikesByPost(Long postId) {
        return likeRepository.findByPostId(postId);
    }

    public boolean isPostLikedByUser(Long userId, Long postId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }
    public List<FriendDTO> getUsersWhoLikedPost(Long postId) {
    List<Like> likes = likeRepository.findByPostId(postId);
    return likes.stream()
                .map(Like::getUser)
                .toList();
}

}
