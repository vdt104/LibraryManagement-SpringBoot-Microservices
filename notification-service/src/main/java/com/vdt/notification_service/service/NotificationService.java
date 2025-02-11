package com.vdt.notification_service.service;

import com.vdt.notification_service.dto.NotificationDTO;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notificationDto);
}
