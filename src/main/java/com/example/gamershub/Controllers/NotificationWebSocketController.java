package com.example.gamershub.Controllers;

import com.example.gamershub.Services.NotificationService;
import com.example.gamershub.dto.NotificationDTO;
import com.example.gamershub.entity.Notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NotificationWebSocketController {

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/sendNotification")
    public void receiveNotification(NotificationDTO dto) {
        notificationService.sendNotification(dto);
    }
    @GetMapping("Notification/user/{userId}")
    @ResponseBody
    public List<Notification> getUserNotifications(@PathVariable Long userId) {
        return notificationService.fetchNotifications(userId);
    }
    
}
