package com.example.gamershub.securityconfig;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.gamershub.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY_STRING;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    }


    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.get("id", Long.class);
        } catch (Exception e) {
            return null; 
        }
    }
    
    //fonction pour extract nom utilisateur  avec token 
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, User user) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    
            return claims.getSubject().equals(user.getEmail()) && claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
