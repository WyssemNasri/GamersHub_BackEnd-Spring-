package com.example.gamershub.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 

    @Column(name = "email", unique = true, length = 50)
    private String email; 

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstName; 

    @Column(name = "lastName")
    private String lastName; 

    @Column(name = "phoneNumber", unique = true)
    private String phoneNumber; 

    @Column(name = "dayOfBirth")
    private Date dayOfBirth; 

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

}
