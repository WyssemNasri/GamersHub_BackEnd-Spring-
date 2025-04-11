package com.example.gamershub.Controllers;

import com.example.gamershub.Respositroys.MessageRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.MessageRequest;
import com.example.gamershub.entity.Message;
import com.example.gamershub.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MessageWebSocketController {

        @Autowired
    private  MessageRepository messageRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  SimpMessagingTemplate messagingTemplate;

    // Endpoint WebSocket pour envoyer un message
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageRequest request) {

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());

        // createdAt sera automatiquement initialisé grâce à @PrePersist
        messageRepository.save(message);

        // Envoyer le message à l'utilisateur cible (receiver)
        messagingTemplate.convertAndSendToUser(
    request.getReceiverId().toString(), // Erreur si getReceiverId() retourne un long primitif
    "/queue/messages",
    message
);

    }
    @GetMapping("/api/messages/last/{userId}")
public ResponseEntity<List<Map<String, Object>>> getLastMessages(@PathVariable Long userId) {
    List<User> friends = userRepository.findFriendsByUserId(userId);
    List<Map<String, Object>> lastMessages = new ArrayList<>();

    for (User friend : friends) {
        Optional<Message> lastMessage = messageRepository.findTopBySenderIdAndReceiverIdOrderByCreatedAtDesc(userId, friend.getId());
        if (lastMessage.isEmpty()) {
            lastMessage = messageRepository.findTopBySenderIdAndReceiverIdOrderByCreatedAtDesc(friend.getId(), userId);
        }

        if (lastMessage.isPresent()) {
            Map<String, Object> msgInfo = new HashMap<>();
            msgInfo.put("friendId", friend.getId());
            msgInfo.put("friendFirstName", friend.getFirstName());
            msgInfo.put("friendLastName", friend.getLastName());
            msgInfo.put("profilePicture", friend.getProfilePicture());
            msgInfo.put("lastMessage", lastMessage.get().getContent());
            msgInfo.put("timestamp", lastMessage.get().getCreatedAt());
            lastMessages.add(msgInfo);
        }
    }

    return ResponseEntity.ok(lastMessages);
}


    
}
