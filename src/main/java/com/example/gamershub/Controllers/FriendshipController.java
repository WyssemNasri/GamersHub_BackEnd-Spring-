package com.example.gamershub.Controllers;
import com.example.gamershub.Services.FriendshipService;
import com.example.gamershub.entity.Friendship;
import com.example.gamershub.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    
    @GetMapping("/{userId}")
    public List<Friendship> getFriends(@PathVariable Long userId) {
        User user = new User(); 
        user.setId(userId);
        return friendshipService.getFriends(user);
    }
}
