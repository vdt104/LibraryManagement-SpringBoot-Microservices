package com.vdt.notification_service.mapper;

import com.vdt.notification_service.dto.NotificationDTO;
import com.vdt.notification_service.entity.Notification;

public class NotificationMapper {
    
    public static NotificationDTO toDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setUserId(notification.getUserId());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setRead(notification.getIsRead());

        return notificationDTO;
    }

    public static Notification toEntity(NotificationDTO notificationDTO) {
        Notification notification = new Notification();

        notification.setUserId(notificationDTO.getUserId());
        notification.setMessage(notificationDTO.getMessage());
        notification.setIsRead(notificationDTO.isRead());

        return notification;
    }
}
