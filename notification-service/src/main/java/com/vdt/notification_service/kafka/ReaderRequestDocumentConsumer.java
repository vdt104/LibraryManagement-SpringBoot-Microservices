package com.vdt.notification_service.kafka;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vdt.notification_service.dto.NotificationDTO;
import com.vdt.notification_service.service.NotificationService;
import com.vdt.reader_request_service.event.ReaderRequestDocumentEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReaderRequestDocumentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderRequestDocumentConsumer.class);

    private final NotificationService notificationService;

    private final RestTemplate restTemplate;

    @KafkaListener(
            topics = "${ReaderRequestDocument.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ReaderRequestDocumentEvent event) {
        LOGGER.info(String.format("A reader has requested to borrow documents. Reader ID: %s", event.getUserId()));

        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
            "http://user-service/api/v1/users/librarians/id",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<String>>() {}
        );

        List<String> consumeByUserId = responseEntity.getBody();

        for (String userId : consumeByUserId) {
            NotificationDTO notificationDto = NotificationDTO.builder()
                    .createdBy(event.getUserId())
                    .consumeBy(userId)
                    .message(event.getMessage())
                    .isRead(false)
                    .build();
            notificationService.createNotification(notificationDto);
        }
    }
}
