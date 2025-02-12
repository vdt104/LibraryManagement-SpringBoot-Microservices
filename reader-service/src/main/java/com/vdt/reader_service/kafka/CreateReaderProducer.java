// package com.vdt.reader_service.kafka;

// import org.apache.kafka.clients.admin.NewTopic;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.support.KafkaHeaders;
// import org.springframework.messaging.Message;
// import org.springframework.messaging.support.MessageBuilder;
// import org.springframework.stereotype.Service;

// import com.vdt.reader_service.event.CreateReaderEvent;

// @Service
// public class CreateReaderProducer {

//     private static final Logger LOGGER = LoggerFactory.getLogger(CreateReaderProducer.class);

//     private NewTopic topic;

//     private KafkaTemplate<String, CreateReaderEvent> kafkaTemplate;

//     public CreateReaderProducer(NewTopic topic, KafkaTemplate<String, CreateReaderEvent> kafkaTemplate) {
//         this.topic = topic;
//         this.kafkaTemplate = kafkaTemplate;
//     }

//     public void sendMessage(CreateReaderEvent event) {
//         LOGGER.info(String.format("Create Reader event => %s", event.toString()));

//         Message<CreateReaderEvent> message = MessageBuilder
//                 .withPayload(event)
//                 .setHeader(KafkaHeaders.TOPIC, topic.name())
//                 .build();

//         kafkaTemplate.send(message);
//     }
// }