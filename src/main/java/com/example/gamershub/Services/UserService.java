package com.example.gamershub.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.dto.UserDto;
import com.example.gamershub.entity.User;

import java.time.LocalDateTime;
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDto adduser(UserDto userdto) {
        if (userRepository.existsByEmail(userdto.getEmail())) {
            throw new RuntimeException("Email already in use");
        } else {
           
            String hashedPassword = passwordEncoder.encode(userdto.getPassword());
            userdto.setPassword(hashedPassword);
            

            User user = userdto.toUser();
            user.setCreatedAt(LocalDateTime.now());


            User savedUser = userRepository.save(user);

            return new UserDto(null, null, 
                               savedUser.getFirstName(),savedUser.getLastName(), 
                               null, null);
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
