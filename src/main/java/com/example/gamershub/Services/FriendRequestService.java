package com.example.gamershub.Services;

import com.example.gamershub.Respositroys.FriendRequestRepository;
import com.example.gamershub.Respositroys.FriendshipRepository;
import com.example.gamershub.Respositroys.NotificationRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.NotificationDTO;
import com.example.gamershub.entity.FriendRequest;
import com.example.gamershub.entity.Friendship;
import com.example.gamershub.entity.Notification;
import com.example.gamershub.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired 
    private UserRepository userRepository ; 
    @Autowired
    private NotificationRepository notificationRepository ; 
    @Autowired
    private final FriendRequestRepository friendRequestRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }



    //fonction pour envoyer une demande d'ami

    public FriendRequest sendFriendRequest(User sender, User receiver) {
        Optional<FriendRequest> existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        if (existingRequest.isPresent()) {
            throw new IllegalStateException("Friend request already exists");
        }
    
        FriendRequest friendRequest = new FriendRequest(sender, receiver, FriendRequest.RequestStatus.PENDING);
        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
    
        // Création de l'objet NotificationDTO
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setSenderId(sender.getId());
        notificationDTO.setReceiverId(receiver.getId());
        notificationDTO.setMessage( userRepository.findById(sender.getId()).getFirstName() +userRepository.findById(sender.getId()).getLastName() + " vous a envoyé une demande d'ami.");
        notificationDTO.setType("FriendRequestSent");
    
        // Étape 1 : Enregistrer la notification dans la base de données
        notificationService.saveNotification(notificationDTO);
    
        // Étape 2 : Envoyer la notification via WebSocket
        notificationService.sendNotification(notificationDTO);
    
        return savedRequest;
    }
    

    //fonction pour obtenir les demandes d'ami envoyées
    public List<FriendRequest> getSentFriendRequests(User sender) {
        return friendRequestRepository.findBySenderAndStatus(sender, "PENDING");
    }
    

    public List<FriendRequest> getReceivedFriendRequests(User receiver) {
        return friendRequestRepository.findByReceiver(receiver);
    }
    public FriendRequest.RequestStatus getFriendRequestStatus(Long senderId, Long receiverId) {
        FriendRequest request = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        return request != null ? request.getStatus() : null;
    }

    public FriendRequest acceptFriendRequest(Long requestId) {
        Optional<FriendRequest> requestOpt = friendRequestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new IllegalStateException("Friend request not found");
        }
    
        FriendRequest request = requestOpt.get();
        if (request.getStatus() != FriendRequest.RequestStatus.PENDING) {
            throw new IllegalStateException("Friend request is already processed");
        }
    
        request.setStatus(FriendRequest.RequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
    
        // Vérifier si l'amitié existe déjà
        if (friendshipRepository.existsBySenderAndReceiver(request.getSender(), request.getReceiver()) ||
            friendshipRepository.existsBySenderAndReceiver(request.getReceiver(), request.getSender())) {
            throw new IllegalStateException("Friendship already exists");
        }
    
        // Enregistrer l'amitié dans la table Friendship
        Friendship friendship = new Friendship(request.getSender(), request.getReceiver());
        friendshipRepository.save(friendship);
    
        // 🔔 Créer et enregistrer une notification pour le sender (celui qui a envoyé la demande)
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setSenderId(request.getReceiver().getId()); // celui qui accepte devient l'émetteur de la notif
        notificationDTO.setReceiverId(request.getSender().getId()); // celui qui a envoyé reçoit la notif
        notificationDTO.setMessage(request.getReceiver().getFirstName() + " " + request.getReceiver().getLastName() + " a accepté votre demande d'ami.");
        notificationDTO.setType("FriendRequestAccepted");
    
        // Enregistrement + Envoi WebSocket
        notificationService.saveNotification(notificationDTO);
        notificationService.sendNotification(notificationDTO);
    
        return request;
    }
    
    // Refuser une demande d'ami
    public FriendRequest rejectFriendRequest(Long requestId) {
        Optional<FriendRequest> requestOpt = friendRequestRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new IllegalStateException("Friend request not found");
        }
        FriendRequest request = requestOpt.get();
        if (request.getStatus() != FriendRequest.RequestStatus.PENDING) {
            throw new IllegalStateException("Friend request is already processed");
        }
        request.setStatus(FriendRequest.RequestStatus.REJECTED);
        return friendRequestRepository.save(request);
    }
    
}
