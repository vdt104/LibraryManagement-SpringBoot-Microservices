package com.vdt.notification_service.repostiory;

import com.vdt.notification_service.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}