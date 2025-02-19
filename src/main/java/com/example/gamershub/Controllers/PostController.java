package com.example.gamershub.Controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.gamershub.Services.PostService;
import com.example.gamershub.dto.PostRequest;
import com.example.gamershub.entity.Post;
import java.io.IOException;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/statue", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Post> createPost(
        @RequestPart("post") PostRequest postRequest,
        @RequestPart("file") MultipartFile file) {
    try {
        Post savedPost = postService.createPost(postRequest, file);
        return ResponseEntity.ok(savedPost);
    } catch (IOException e) {
        return ResponseEntity.internalServerError().build();
    }
}
}
