package com.example.gamershub.Services;

import com.example.gamershub.Respositroys.CommentRepository;
import com.example.gamershub.Respositroys.FriendshipRepository;
import com.example.gamershub.Respositroys.LikeRepository;
import com.example.gamershub.Respositroys.PostRepository;
import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.FriendDTO;
import com.example.gamershub.entity.Comment;
import com.example.gamershub.entity.Friendship;
import com.example.gamershub.entity.Like;
import com.example.gamershub.entity.Post;
import com.example.gamershub.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    @Autowired 
    private LikeService likeService;
    @Autowired
    CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository; 

    @Autowired
    private CommentRepository commentRepository;

    public List<FriendDTO> getFriends(User user) {
        List<Friendship> friendships = friendshipRepository.findBySenderOrReceiver(user, user);

        return friendships.stream()
                .map(friendship -> {
                    User friend = friendship.getSender().getId() == user.getId()
                            ? friendship.getReceiver()
                            : friendship.getSender();

                    return new FriendDTO(
                            friend.getId(),
                            friend.getFirstName(),
                            friend.getLastName(),
                            friend.getProfilePicture());
                })
                .collect(Collectors.toList());
    }

    public List<Post> getFriendsPosts(User user) {
        // Obtenir les amis de l'utilisateur sous forme de FriendDTO
        List<FriendDTO> friends = getFriends(user); // Assurez-vous que cette méthode renvoie des FriendDTO
        List<Post> friendsPosts = new ArrayList<>();
    
        for (FriendDTO friend : friends) {
            // Récupérer tous les posts de l'ami
            List<Post> posts = postRepository.findByUserId(friend.getId());
    
            for (Post post : posts) {
                // Récupérer les commentaires du post
                List<Comment> comments = commentService.getCommentsByPostId(post.getId());
                post.setComments(comments);
    
                // Récupérer le nombre de likes
                int likeCount = likeService.getLikesByPost(post.getId()).size();  // Nombre de likes
                post.setLikeCount(likeCount);
    
                // Récupérer les utilisateurs qui ont liké le post (avec FriendDTO)
                List<FriendDTO> usersWhoLiked = likeService.getUsersWhoLikedPost(post.getId());
                post.setUsersWhoLiked(usersWhoLiked);
    
                // Ajouter l'utilisateur propriétaire du post (utilisation de FriendDTO ici si nécessaire)
                User postOwner = post.getUser();
                FriendDTO postOwnerDTO = postOwner.toFriendDTO(user);  // Utilisation de la méthode pour convertir User en FriendDTO
                post.setUserDTO(postOwnerDTO);  // Assurez-vous que setUserDTO est une méthode valide dans Post
            }
    
            // Ajouter les posts de l'ami à la liste des posts
            friendsPosts.addAll(posts);
        }
    
        return friendsPosts;
    }
    

}
