package com.vdt.notification_service.kafka;

import com.vdt.notification_service.dto.NotificationDTO;
import com.vdt.notification_service.service.NotificationService;
import com.vdt.user_service.event.CreateReaderEvent;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReaderConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReaderConsumer.class);

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(CreateReaderEvent event) {
        LOGGER.info(String.format("Create Reader event received in notification service => %s", event.toString()));

        // Chuyển đổi CreateReaderEvent thành NotificationDTO
        NotificationDTO notificationDto = NotificationDTO.builder()
                .message(event.getMessage())
                .userId(event.getReader().getUser().getId())
                .build();

        notificationService.createNotification(notificationDto);
    }
}