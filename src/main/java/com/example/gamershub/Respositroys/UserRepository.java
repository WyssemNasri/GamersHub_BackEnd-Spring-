package com.example.gamershub.Respositroys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gamershub.entity.User;



public interface UserRepository extends JpaRepository<User,Long>{
    boolean existsByEmail(String email);
    User findByEmail(String email);
   
    
}
