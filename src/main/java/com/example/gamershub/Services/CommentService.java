package com.example.gamershub.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.gamershub.Respositroys.CommentRepository;
import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.CommentRequestDTO;
import com.example.gamershub.entity.Comment;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;
@Service
public class CommentService {


    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userrepository;

    CommentService(CommentRepository commentRepository , PostRepository postRepository , UserRepository userrepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userrepository = userrepository;
    }


    //Fonction pour recuperer les commentaires d'une poste
    
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
    
    //Fonction pour ajouter un Commentaire sur une poste 
    public void addComment(CommentRequestDTO commentRequestDTO) {
    User user = userrepository.findById(commentRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    Post post = postRepository.findById(commentRequestDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
    Comment comment = new Comment(user, post, commentRequestDTO.getContent());
    commentRepository.save(comment);
}

// Retourner tous les commentaires d'un post (avec user et content)
public List<Comment> getCommentsByPost(Long postId) {
    return commentRepository.findByPostId(postId);
}



}
