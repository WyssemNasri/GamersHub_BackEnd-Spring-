package com.example.gamershub.Services;

import com.example.gamershub.Respositroys.LikeRepository;
import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.FriendDTO;
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

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // Ajouter un like
    public Like addLike(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Like like = new Like(user, post, true);
        return likeRepository.save(like);
    }

    // Supprimer un like
    public void deleteLike(Long likeId) {
        Like like = likeRepository.findById(likeId)
                .orElseThrow(() -> new EntityNotFoundException("Like not found"));
        likeRepository.delete(like);
    }

    // Afficher les likes d'un post
    public List<Like> getLikesByPost(Long postId) {
        return likeRepository.findByPostId(postId);
    }

    // Vérifier si un user a liké un post
    public boolean isPostLikedByUser(Long userId, Long postId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }

    // Retourner la liste des utilisateurs qui ont liké un post
public List<FriendDTO> getUsersWhoLikedPost(Long postId) {
    List<Like> likes = likeRepository.findByPostId(postId);
    return likes.stream()
                .map(Like::getUser)
                .toList();
}

}
