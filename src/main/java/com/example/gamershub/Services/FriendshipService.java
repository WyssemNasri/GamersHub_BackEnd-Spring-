package com.example.gamershub.Services;


import com.example.gamershub.Respositroys.FriendshipRepository;
import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.FriendDTO;
import com.example.gamershub.entity.Friendship;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private PostRepository postRepository;

    public List<FriendDTO> getFriends(User user) {
    List<Friendship> friendships = friendshipRepository.findBySenderOrReceiver(user, user);

    return friendships.stream()
            .map(friendship -> {
                
                User friend = friendship.getSender().getId() == user.getId()
                        ? friendship.getReceiver()
                        : friendship.getSender();

                return new FriendDTO(
                        friend.getId(),
                        friend.getFirstName(),
                        friend.getLastName(),
                        friend.getProfilePicture()
                );
            })
            .collect(Collectors.toList());
}

    
    

public List<Post> getFriendsPosts(User user) {
    List<FriendDTO> friends = getFriends(user);

    List<Post> friendsPosts = new ArrayList<>();

    for (FriendDTO friend : friends) {
        List<Post> posts = postRepository.findByUserId(friend.getId());

        for (Post post : posts) {
            // Retrieve the User entity from the database
            User postOwner = userRepository.findById(post.getUser().getId()).orElse(null);

            if (postOwner != null) {
                post.setUser(postOwner); // Set the User entity to the Post
            }
        }

        friendsPosts.addAll(posts);
    }

    return friendsPosts;
}


    
    
    
    
}
