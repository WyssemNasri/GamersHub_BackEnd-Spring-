package com.example.gamershub.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.gamershub.Services.AuthService;
import com.example.gamershub.dto.AuthRequest;
import com.example.gamershub.dto.AuthResponse;
import com.example.gamershub.entity.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        String token = authService.authenticate(authRequest.getEmail(), authRequest.getPassword());
        if (token != null) {
            User user = authService.getUserByEmail(authRequest.getEmail());
            if (user != null) {
                AuthResponse authResponse = new AuthResponse(token);
                return ResponseEntity.ok(authResponse);
            }
        }
        return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
    }
}
