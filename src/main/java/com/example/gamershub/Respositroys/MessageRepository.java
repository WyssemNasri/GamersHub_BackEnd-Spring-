package com.example.gamershub.Respositroys;

import com.example.gamershub.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// Interface pour gérer les opérations avec la table Message
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Récupérer tous les messages entre 2 utilisateurs triés par date croissante
    List<Message> findBySenderIdAndReceiverIdOrderByCreatedAtAsc(Long senderId, Long receiverId);

    // Récupérer le dernier message échangé entre 2 utilisateurs
    Message findTopBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtDesc(
            Long senderId1, Long receiverId1, Long senderId2, Long receiverId2
    );
    Optional<Message> findTopBySenderIdAndReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);

}
