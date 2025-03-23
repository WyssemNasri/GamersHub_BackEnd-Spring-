package com.example.gamershub.Respositroys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gamershub.entity.Friendship;
import com.example.gamershub.entity.User;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    boolean existsBySenderAndReceiver(User sender, User receiver);
    List<Friendship> findBySenderOrReceiver(User sender, User receiver);
}

