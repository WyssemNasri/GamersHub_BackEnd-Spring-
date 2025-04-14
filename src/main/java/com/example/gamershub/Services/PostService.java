package com.example.gamershub.Services;

import com.example.gamershub.Respositroys.CommentRepository;
import com.example.gamershub.Respositroys.LikeRepository;
import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.FriendDTO;
import com.example.gamershub.dto.PostRequest;
import com.example.gamershub.entity.Comment;
import com.example.gamershub.entity.Like;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {


    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public PostService(PostRepository postRepository, UserRepository userRepository, FriendshipService friendshipService , LikeRepository likeRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public List<Post> fetchUserPosts(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public Post createPost(PostRequest postRequest, MultipartFile file) throws IOException {
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        String urlMedia = null;
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            urlMedia = fileName;

        }

        Post post = new Post();
        post.setUser(user);
        post.setDescription(postRequest.getDescription());
        post.setUrlMedia(urlMedia);

        return postRepository.save(post);
    }
    @Transactional
public void likePost(Long userId, Long postId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

    // Use Optional to check if the like exists
    Like existingLike = likeRepository.findByUserAndPost(user, post);

    if (existingLike != null) {
        // If the user already liked the post, remove the like (dislike the post)
        likeRepository.delete(existingLike);
    } else {
        // If the user hasn't liked the post yet, add a new like
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
    }
}
public Comment addComment(Long postId, Long userId, String content) {
    Optional<Post> postOptional = postRepository.findById(postId);
    Optional<User> userOptional = userRepository.findById(userId);

    if (postOptional.isEmpty() || userOptional.isEmpty()) {
        throw new RuntimeException("Post or User not found");
    }

    Post post = postOptional.get();
    User user = userOptional.get();

    Comment comment = new Comment();
    comment.setPost(post);
    comment.setUser(user);
    comment.setContent(content);

    return commentRepository.save(comment);
}
public List<Post> fetchVideoPosts() {
    // Récupérer toutes les publications qui ont une URL média se terminant par une extension vidéo.
    List<Post> allPosts = postRepository.findAll();
    return allPosts.stream()
            .filter(post -> post.getUrlMedia() != null && 
                    (post.getUrlMedia().endsWith(".mp4") || post.getUrlMedia().endsWith(".avi") || post.getUrlMedia().endsWith(".mov")))
            .collect(Collectors.toList());
}


}
