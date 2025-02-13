package com.vdt.notification_service.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.vdt.notification_service.dto.NotificationDTO;
import com.vdt.notification_service.entity.Notification;
import com.vdt.notification_service.mapper.NotificationMapper;
import com.vdt.notification_service.repository.NotificationRepository;
import com.vdt.notification_service.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationDTO> getNotificationsByUserId(String userId) {
        List<Notification> notifications = notificationRepository.findByConsumeBy(userId);
        return notifications.stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDto) {
        Notification notification = NotificationMapper.toEntity(notificationDto);

        notificationRepository.save(notification);
 
        return NotificationMapper.toDTO(notification); 
    }
}