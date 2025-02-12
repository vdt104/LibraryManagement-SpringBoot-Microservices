package com.vdt.notification_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.vdt.notification_service.dto.NotificationDTO;
import com.vdt.notification_service.service.NotificationService;
import com.vdt.reader_service.event.CreateReaderEvent;

import lombok.RequiredArgsConstructor;

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
        LOGGER.info(String.format("A new guest has requested to create a reader account with user id: %s", event.getUserId()));
        
        NotificationDTO notificationDto = NotificationDTO.builder()
                .userId(event.getUserId())
                .message("A new guest has requested to create a reader account")
                .isRead(false)
                .build();

        notificationService.createNotification(notificationDto);
    }
}