package com.example.gamershub.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gamershub.Respositroys.FriendshipRepository;
import com.example.gamershub.entity.Friendship;
import com.example.gamershub.entity.User;
@Service
public class FriendshipService  {
    
    @Autowired
    private FriendshipRepository friendshipRepository ;
    public List<Friendship> getFriends(User user) {
        return friendshipRepository.findBySenderOrReceiver(user, user);
    }
}
