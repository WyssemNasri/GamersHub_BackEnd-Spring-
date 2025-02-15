package com.example.gamershub.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.gamershub.Respositroys.UserRepository;
import com.example.gamershub.entity.User;
import com.example.gamershub.securityconfig.JwtUtil;
@Service
public class AuthService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());
            saveTokenToDatabase(user.getId(), token);
            return token;
        }
        return null;
    }

    private void saveTokenToDatabase(Long userId, String token) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setToken(token);
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
