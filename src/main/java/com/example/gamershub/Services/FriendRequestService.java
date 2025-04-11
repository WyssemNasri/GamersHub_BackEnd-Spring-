package com.example.gamershub.Services;

import com.example.gamershub.Respositroys.FriendRequestRepository;
import com.example.gamershub.Respositroys.FriendshipRepository;
import com.example.gamershub.entity.FriendRequest;
import com.example.gamershub.entity.Friendship;
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
        return friendRequestRepository.save(friendRequest);
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

        // Mettre à jour le statut de la demande d'ami
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
