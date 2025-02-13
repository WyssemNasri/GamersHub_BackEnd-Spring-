package com.example.gamershub.Controllers;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamershub.Services.UserService;
import com.example.gamershub.dto.UserDto;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userservice ; 
    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        try {
            UserDto savedUser = userservice.adduser(userDto);
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
