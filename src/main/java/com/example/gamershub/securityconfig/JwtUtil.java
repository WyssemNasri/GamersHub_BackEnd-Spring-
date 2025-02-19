package com.example.gamershub.securityconfig;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil  {
        private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
        public String generateToken(String username) {
            return Jwts.builder()
                    .subject(username)  
                    .issuedAt(new Date())  
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
                    .signWith(SECRET_KEY) 
                    .compact();
        }
}
