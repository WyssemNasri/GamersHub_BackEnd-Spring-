package com.example.gamershub.Respositroys;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.gamershub.entity.User;



public interface UserRepository extends JpaRepository<User,Long>{
    User findById(long id);
    boolean existsByEmail(String email);
    User findByEmail(String email);
    List <User>findAll() ; 
    

    // Exemple : récupérer les amis d'un utilisateur donné
@Query("SELECT u FROM User u JOIN Friendship f ON (f.sender = u OR f.receiver = u) WHERE f.sender.id = :userId OR f.receiver.id = :userId")
List<User> findFriendsByUserId(@Param("userId") Long userId);


   
    
}
