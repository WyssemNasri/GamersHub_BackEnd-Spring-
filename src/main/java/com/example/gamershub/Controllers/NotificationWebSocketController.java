package com.example.gamershub.Controllers;

import com.example.gamershub.Services.NotificationService;
import com.example.gamershub.dto.NotificationDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationWebSocketController {

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/sendNotification")
    public void receiveNotification(NotificationDTO dto) {
        notificationService.sendNotification(dto);
    }
    
}
