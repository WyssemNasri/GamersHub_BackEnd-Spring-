package com.example.gamershub.securityconfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // Pour envoyer vers le client
        config.setApplicationDestinationPrefixes("/app"); // Pour recevoir du client
        config.setUserDestinationPrefix("/user"); // Pour messages privés
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS(); // Sans SockJS
        // registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS(); // (si Flutter utilise SockJS)
    }



}
