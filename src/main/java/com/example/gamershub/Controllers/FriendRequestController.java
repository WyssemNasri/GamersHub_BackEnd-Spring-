package com.example.gamershub.Controllers;
import com.example.gamershub.Services.FriendRequestService;
import com.example.gamershub.dto.FriendRequestRequestDTO;
import com.example.gamershub.entity.FriendRequest;
import com.example.gamershub.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendRequestController {
    @Autowired
    private FriendRequestService friendRequestService;
    @PostMapping("/sendRequest")
    
public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestRequestDTO requestDTO) {
    try {
        User sender = new User(); 
        sender.setId(requestDTO.getSenderId());

        User receiver = new User(); 
        receiver.setId(requestDTO.getReceiverId());
        friendRequestService.sendFriendRequest(sender, receiver);

        return ResponseEntity.ok("Friend request sent successfully");
    } catch (IllegalStateException e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<FriendRequest>> getSentFriendRequests(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);

        List<FriendRequest> requests = friendRequestService.getReceivedFriendRequests(user);
        return ResponseEntity.ok(requests);
    }
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<FriendRequest>> getReceivedFriendRequests(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);

        List<FriendRequest> requests = friendRequestService.getReceivedFriendRequests(user);
        return ResponseEntity.ok(requests);
    }
    @GetMapping("/status")
    public ResponseEntity<FriendRequest.RequestStatus> getFriendRequestStatus(
            @RequestParam Long senderId, 
            @RequestParam Long receiverId) {
        FriendRequest.RequestStatus status = friendRequestService.getFriendRequestStatus(senderId, receiverId);
        return ResponseEntity.ok(status);
}
@PostMapping("/accept/{requestId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long requestId) {
        try {
            friendRequestService.acceptFriendRequest(requestId);
            return ResponseEntity.ok("Friend request accepted successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Refuser une demande d'ami
    @PostMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long requestId) {
        try {
            friendRequestService.rejectFriendRequest(requestId);
            return ResponseEntity.ok("Friend request rejected successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}