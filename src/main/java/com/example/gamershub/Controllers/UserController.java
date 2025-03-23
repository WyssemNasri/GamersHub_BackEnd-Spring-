package com.example.gamershub.Controllers;

import com.example.gamershub.Services.UserService;
import com.example.gamershub.dto.UserDto;
import com.example.gamershub.dto.UserResponse;
import com.example.gamershub.entity.User;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/all")
    public ResponseEntity<List<User>> getallUsers(){
        return ResponseEntity.ok(userService.getAllUsers()) ; 
    }

    private String extractDuplicateFieldErrorMessage(String errorMessage, UserDto userDto) {
        if (errorMessage.contains("phone_number")) {
            return "Le numéro " + userDto.getPhoneNumber() + " est déjà utilisé par un autre compte.";
        } else if (errorMessage.contains("email")) {
            return "L'email " + userDto.getEmail() + " est déjà utilisé par un autre compte.";
        }
        return "Ce compte existe déjà.";
    }
    @PostMapping("/uploadprofilepic")
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                       @RequestParam("userId") Long userId) {
        try {
            String profilePicUrl = userService.saveProfilePicture(file, userId);
            return "Profile picture uploaded successfully: " + profilePicUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload profile picture";
        }
    }
    
    @PostMapping("/uploadcoverpic")
    public String uploadCoverPicture(@RequestParam("file") MultipartFile file,
                                     @RequestParam("userId") Long userId) {
        try {
            String coverPicUrl = userService.saveCoverPicture   (file, userId);
            return "Cover picture uploaded successfully: " + coverPicUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload cover picture";
        }
    }
     @GetMapping("/details/{userId}")
    public ResponseEntity<UserResponse> getUserDetails(@PathVariable Long userId) {
        UserResponse userDetails = userService.getUserDetails(userId);
        return ResponseEntity.ok(userDetails);
    }

    
}       
            