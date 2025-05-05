package com.example.gamershub.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.gamershub.Respositroys.CommentRepository;
import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.CommentRequestDTO;
import com.example.gamershub.dto.NotificationDTO;
import com.example.gamershub.entity.Comment;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userepository,
            NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userepository;
        this.notificationService = notificationService;
    }

    // Fonction pour recuperer les commentaires d'une poste

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // Fonction pour ajouter un Commentaire sur une poste
    public void addComment(CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findById(commentRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(commentRequestDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment(user, post, commentRequestDTO.getContent());
        commentRepository.save(comment);

        if (user.getId() != post.getUser().getId()) { // Use != for comparing primitive long values
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setSenderId(user.getId());
            notificationDTO.setReceiverId(post.getUser().getId());
            notificationDTO.setMessage(user.getFirstName() + " a commenté sur votre publication.");
            notificationDTO.setType("PostCommented");

            notificationService.saveNotification(notificationDTO); 
            notificationService.sendNotification(notificationDTO); 
        }

    }
}
