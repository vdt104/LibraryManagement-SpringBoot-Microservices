package com.vdt.reader_request_service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.vdt.reader_request_service.event.ReaderRequestDocumentEvent;

@Service
public class ReaderRequestDocumentProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderRequestDocumentProducer.class);

    private NewTopic topic;

    private KafkaTemplate<String, ReaderRequestDocumentEvent> kafkaTemplate;

    public ReaderRequestDocumentProducer(NewTopic topic, KafkaTemplate<String, ReaderRequestDocumentEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ReaderRequestDocumentEvent event) {
        LOGGER.info(String.format("Reader Request Document event => %s", event.toString()));

        Message<ReaderRequestDocumentEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        kafkaTemplate.send(message);
    }
}