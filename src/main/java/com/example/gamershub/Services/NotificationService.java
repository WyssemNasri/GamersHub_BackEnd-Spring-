package com.example.gamershub.Services;

import com.example.gamershub.Respositroys.NotificationRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.NotificationDTO;
import com.example.gamershub.entity.Notification;
import com.example.gamershub.entity.User;

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
     * Envoie une notification d'un utilisateur vers un autre.
     * Sauvegarde la notification en base de donn es et envoie
     * une notification via WebSocket uniquement des donn es utiles,
     *   savoir le type, le message, l'exp diteur et le destinataire.
     * @param dto Les donn es de la notification  envoyer.
     */
    @Transactional
public void sendNotification(NotificationDTO dto) {
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

    notificationRepository.save(notification);

    // Envoi via WebSocket uniquement des données utiles
    messagingTemplate.convertAndSend(
            "/topic/notifications/" + receiver.getId(),
            dto
    );
}

}
