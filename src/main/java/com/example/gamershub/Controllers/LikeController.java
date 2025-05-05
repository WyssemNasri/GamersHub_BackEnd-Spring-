package com.example.gamershub.Controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.example.gamershub.Services.LikeService;
import com.example.gamershub.dto.LikeRequest;
import com.example.gamershub.entity.Like;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/Like")
public class LikeController {
    @Autowired
   private LikeService likeService; 

   @GetMapping("getLikesbypost/{postid}")
   public List<Like> getLikesbypost(@PathVariable Long postid){
    return likeService.getLikesByPost(postid);  
   }  
    
      @PostMapping("/like")
    public ResponseEntity<String> likePost(@RequestBody LikeRequest likeRequest) {
        try {
            likeService.likePost(likeRequest);
            return ResponseEntity.ok("Post liked successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
