package com.vdt.notification_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.vdt.notification_service.entity.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByConsumeBy(String consumeBy);
}