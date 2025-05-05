    package com.example.gamershub.Controllers;

    import com.example.gamershub.dto.MessageRequest;
import com.example.gamershub.dto.MessageResponseDto;
import com.example.gamershub.entity.Message;
    import com.example.gamershub.entity.User;
    import com.example.gamershub.Respositroys.MessageRepository;
    import com.example.gamershub.Respositroys.UserRepository;

    import java.util.List;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.messaging.handler.annotation.MessageMapping;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;

    @Controller
    public class MessageWebSocketController {

        @Autowired
        private MessageRepository messageRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private SimpMessagingTemplate messagingTemplate;

        @MessageMapping("/chat.sendMessage")
        public void sendMessage(MessageRequest request) {
            User sender = userRepository.findById(request.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
            User receiver = userRepository.findById(request.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(request.getContent());
            messageRepository.save(message);
            messagingTemplate.convertAndSendToUser(Integer.toString((int) receiver.getId()),
                    "/queue/messages",
                    message);
        }

        @GetMapping("/api/messages/last/{userId}/{friendId}")
public ResponseEntity<MessageResponseDto> getLastMessage(@PathVariable Long userId, @PathVariable Long friendId) {
    List<Message> messages = messageRepository
            .findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAtDesc(
                    userId, friendId, userId, friendId);

    if (messages.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    Message lastMessage = messages.get(0);
    return ResponseEntity.ok(new MessageResponseDto(lastMessage));
}

@GetMapping("/api/messages/{userId}/{friendId}")
public ResponseEntity<List<MessageResponseDto>> getAllMessages(
        @PathVariable Long userId,
        @PathVariable Long friendId) {

    List<Message> messages = messageRepository
            .findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAtAsc(
                    userId, friendId, userId, friendId);

    if (messages.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    List<MessageResponseDto> response = messages.stream()
            .map(MessageResponseDto::new)
            .toList();

    return ResponseEntity.ok(response);
}


    }
