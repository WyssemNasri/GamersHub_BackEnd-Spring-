package com.example.gamershub.securityconfig;

import com.example.gamershub.Services.UserService;
import com.example.gamershub.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
    
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
    
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.getUserByEmail(email);
    
            if (user != null && jwtUtil.validateToken(token, user)) {
                // Create Authentication object with the User entity as principal
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, 
                        null, 
                        new ArrayList<>()  // No authorities if you're not using roles
                );
    
                // Cast to UsernamePasswordAuthenticationToken to access setDetails method
                ((UsernamePasswordAuthenticationToken) authentication)
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
                // Set Authentication object into the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    
        filterChain.doFilter(request, response);
    }
    

}
