package com.vdt.notification_service.service;

import java.util.List;

import com.vdt.notification_service.dto.NotificationDTO;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notificationDto);

    List<NotificationDTO> getNotificationsByUserId(String userId);
}
