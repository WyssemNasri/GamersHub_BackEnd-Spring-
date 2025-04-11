package com.example.gamershub.Controllers;
import com.example.gamershub.Services.PostService;
import com.example.gamershub.dto.PostRequest;
import com.example.gamershub.entity.Comment;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;

    public PostController(PostService postService, ObjectMapper objectMapper) {
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/statueUser/{user_id}")
    public ResponseEntity<List<Post>> fetchUserPosts(@PathVariable Long user_id) {
        return ResponseEntity.ok(postService.fetchUserPosts(user_id));
    }

    @PostMapping(value = "/statue", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestPart("post") String postRequestJson, // Reçoit JSON sous forme de String
            @RequestPart(value = "file", required = false) MultipartFile file) { // Rend le fichier facultatif
        try {
            // Convertir JSON en objet PostRequest
            PostRequest postRequest = objectMapper.readValue(postRequestJson, PostRequest.class);

            // Créer et enregistrer le post
            Post savedPost = postService.createPost(postRequest, file);
            return ResponseEntity.ok(savedPost);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    @PostMapping("/like")
    public ResponseEntity<String> likePost(@RequestParam Long userId, @RequestParam Long postId) {
        try {
            postService.likePost(userId, postId);
            return ResponseEntity.ok("Post liked successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<Comment> addComment(
            @RequestParam Long postId,      // ID du post
            @RequestParam Long userId,      // ID de l'utilisateur
            @RequestParam String content) { // Contenu du commentaire
        try {
            Comment comment = postService.addComment(postId, userId, content);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/videos")
    public ResponseEntity<List<Post>> fetchVideoPosts() {
        List<Post> videoPosts = postService.fetchVideoPosts();
        return ResponseEntity.ok(videoPosts);
    }
    


    

    

    



}
