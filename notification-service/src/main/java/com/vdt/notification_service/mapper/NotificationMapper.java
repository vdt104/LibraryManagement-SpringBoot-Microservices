package com.vdt.notification_service.mapper;

import com.vdt.notification_service.dto.NotificationDTO;
import com.vdt.notification_service.entity.Notification;

public class NotificationMapper {
    
    public static NotificationDTO toDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setConsumeBy(notification.getConsumeBy());
        notificationDTO.setCreatedBy(notification.getCreatedBy());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setRead(notification.getIsRead());

        return notificationDTO;
    }

    public static Notification toEntity(NotificationDTO notificationDTO) {
        Notification notification = new Notification();

        notification.setConsumeBy(notificationDTO.getConsumeBy());
        notification.setCreatedBy(notificationDTO.getCreatedBy());
        notification.setMessage(notificationDTO.getMessage());
        notification.setIsRead(notificationDTO.isRead());

        return notification;
    }
}
