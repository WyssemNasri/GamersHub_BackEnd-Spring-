package com.example.gamershub.Respositroys;

import com.example.gamershub.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
