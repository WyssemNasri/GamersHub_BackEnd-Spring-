package com.example.gamershub.dto;

import java.time.LocalDate;

import com.example.gamershub.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDto {
    
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dayOfBirth; // Ajout du champ dayOfBirth

    public UserDto() {}

    public UserDto(String email, String password, String firstName, String lastName, 
                   String phoneNumber, LocalDate dayOfBirth) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dayOfBirth = dayOfBirth;
    }

    public User toUser() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setPhoneNumber(this.phoneNumber);
        user.setDayOfBirth(java.sql.Date.valueOf(this.dayOfBirth));
        return user;
    }


}
