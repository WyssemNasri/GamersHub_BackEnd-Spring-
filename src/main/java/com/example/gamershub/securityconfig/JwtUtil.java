package com.example.gamershub.securityconfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil  {
        private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
        public String generateToken(String username) {
            return Jwts.builder()
                    .subject(username)  // Using the new 'subject' method
                    .issuedAt(new Date())  // Using the new 'issuedAt' method

                    .signWith(SECRET_KEY)  // Directly passing the key without the SignatureAlgorithm
                    .compact();
        }
}
