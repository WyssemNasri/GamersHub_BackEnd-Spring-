package com.example.gamershub.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.gamershub.Services.CommentService;
import com.example.gamershub.dto.CommentRequestDTO;
@RequestMapping("comments")
@RestController
public class CommentController {

    CommentService commentService;
    CommentController(CommentService commentService) {
        this.commentService = commentService;   
    }
    @GetMapping("/getCommentsByPostId/{postId}")
    public ResponseEntity getCommentByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }
    
     @PostMapping("/add")
     public ResponseEntity<?> addComment(@RequestBody CommentRequestDTO commentDTO) {
         commentService.addComment(commentDTO);
         return ResponseEntity.ok("Comment Added Successfully");
     }
     

}
