package com.example.gamershub.Respositroys;

import com.example.gamershub.entity.FriendRequest;
import com.example.gamershub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    List<FriendRequest> findBySenderAndStatus(User sender, String status);
    List<FriendRequest> findBySender(User sender);
    List<FriendRequest> findByReceiver(User receiver);
    FriendRequest findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
