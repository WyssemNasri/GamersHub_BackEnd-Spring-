package com.example.gamershub.Controllers;

import com.example.gamershub.Services.UserService;
import com.example.gamershub.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            UserDto savedUser = userService.adduser(userDto);
            return ResponseEntity.ok(savedUser);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = extractDuplicateFieldErrorMessage(e.getMessage(), userDto);
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String extractDuplicateFieldErrorMessage(String errorMessage, UserDto userDto) {
        if (errorMessage.contains("phone_number")) {
            return "Le numéro " + userDto.getPhoneNumber() + " est déjà utilisé par un autre compte.";
        } else if (errorMessage.contains("email")) {
            return "L'email " + userDto.getEmail() + " est déjà utilisé par un autre compte.";
        }
        return "Ce compte existe déjà.";
    }
}
            