package com.example.gamershub.Controllers;

import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.Services.FriendshipService;
import com.example.gamershub.dto.FriendDTO;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserRepository userRepository;

    // Constructeur avec injection de dépendances
    FriendshipController(FriendshipService friendshipService, UserRepository userRepository) {
        this.friendshipService = friendshipService;
        this.userRepository = userRepository;
    }

    // Méthode pour récupérer les amis d'un utilisateur
    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendDTO>> getFriends(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build(); // Utilisateur non trouvé
        }

        User user = optionalUser.get();
        List<FriendDTO> friends = friendshipService.getFriends(user);
        return ResponseEntity.ok(friends);
    }

    // Méthode pour récupérer les posts des amis de l'utilisateur
    @GetMapping("/friendsPosts/{userId}")
    public ResponseEntity<List<Post>> getFriendsPosts(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build(); 
        }

        User user = optionalUser.get();
        List<Post> friendsPosts = friendshipService.getFriendsPosts(user);
        return ResponseEntity.ok(friendsPosts);
    }


}
