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

/**
 * Handles the login request.
 *
 * @param authRequest the authentication request containing email and password
 * @return ResponseEntity containing the AuthResponse with a JWT token and user ID
 *         if authentication is successful; otherwise, a 401 status with an error message
 */


    /**
     * Handles the login request.
     *
     * @param authRequest the authentication request containing email and password
     * @return ResponseEntity containing the AuthResponse with a JWT token and user ID
     *         if authentication is successful; otherwise, a 401 status with an error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        // Authenticate the user by email and password
        String token = authService.authenticate(authRequest.getEmail(), authRequest.getPassword());
        if (token != null) {
            // Retrieve the user from the database
            User user = authService.getUserByEmail(authRequest.getEmail());
            if (user != null) {
                // Create the AuthResponse containing the JWT token and user ID
                AuthResponse authResponse = new AuthResponse(token, String.valueOf(user.getId()));  
                // Return the AuthResponse with a 200 status
                return ResponseEntity.ok(authResponse);
            }
        }
        // Return an error message with a 401 status if authentication fails
        return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
    }

}


