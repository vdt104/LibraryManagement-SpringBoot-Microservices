package com.vdt.notification_service.service.impl;

import org.springframework.stereotype.Service;

import com.vdt.notification_service.dto.NotificationDTO;
import com.vdt.notification_service.entity.Notification;
import com.vdt.notification_service.mapper.NotificationMapper;
import com.vdt.notification_service.repostiory.NotificationRepository;
import com.vdt.notification_service.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDto) {
        Notification notification = NotificationMapper.toEntity(notificationDto);

        notificationRepository.save(notification);
 
        return NotificationMapper.toDTO(notification); 
    }

}
