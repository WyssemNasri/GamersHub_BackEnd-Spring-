package com.example.gamershub.Controllers;
import com.example.gamershub.Services.PostService;
import com.example.gamershub.dto.PostRequest;
import com.example.gamershub.entity.Post;
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
    public ResponseEntity<?> createPost(
            @RequestPart("post") String postRequestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            PostRequest postRequest = objectMapper.readValue(postRequestJson, PostRequest.class);
            Post savedPost = postService.createPost(postRequest, file);
            return ResponseEntity.ok(savedPost);

        } catch (RuntimeException e) {
            // Contenu inapproprié ou utilisateur non trouvé
            return ResponseEntity.status(403).body(e.getMessage());

        } catch (IOException e) {
            // Erreur lors de la lecture ou conversion du JSON
            return ResponseEntity.badRequest().body("Erreur lors du traitement de la requête : " + e.getMessage());
        }
    }
    

 


    @GetMapping("/videos")
    public ResponseEntity<List<Post>> fetchVideoPosts() {
        List<Post> videoPosts = postService.fetchVideoPosts();
        return ResponseEntity.ok(videoPosts);
    }
    


    

    

    



}
