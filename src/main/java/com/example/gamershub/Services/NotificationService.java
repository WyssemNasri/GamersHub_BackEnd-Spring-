package com.example.gamershub.Services;

import com.example.gamershub.Respositroys.NotificationRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.NotificationDTO;
import com.example.gamershub.entity.Notification;
import com.example.gamershub.entity.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Enregistre la notification dans la base de données.
     */
    @Transactional
    public Notification saveNotification(NotificationDTO dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Notification notification = Notification.builder()
                .type(dto.getType())
                .message(dto.getMessage())
                .sender(sender)
                .receiver(receiver)
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }

    /**
     * Envoie une notification au client via WebSocket.
     */
    public void sendNotification(NotificationDTO dto) {
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + dto.getReceiverId(),
                dto
        );
    }

    /**
     * Utilitaire : Enregistre + Envoie.
     */
    @Transactional
    public void notify(NotificationDTO dto) {
        saveNotification(dto);
        sendNotification(dto);
    }

    public List<Notification> fetchNotifications(Long receiverId) {
        return notificationRepository.findByReceiverId(receiverId);
    }

    @Transactional
public void markAsRead(Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));

    if (!notification.isRead()) {
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
}