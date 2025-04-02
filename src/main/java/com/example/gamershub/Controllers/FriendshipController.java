package com.example.gamershub.Controllers;
import com.example.gamershub.Services.FriendshipService;
import com.example.gamershub.dto.FriendDTO;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {


    private final FriendshipService friendshipService;
    FriendshipController(FriendshipService friendshipService) {
        this.friendshipService=friendshipService ; 
    }


    @GetMapping("/{userId}")
    public List<FriendDTO> getFriends(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return friendshipService.getFriends(user);
    }
    @GetMapping("/friendsPosts/{userId}")
    public ResponseEntity<List<Post>> getFriendsPosts(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        
        
        List<Post> friendsPosts = friendshipService.getFriendsPosts(user);
        
        return ResponseEntity.ok(friendsPosts);
    }

}
