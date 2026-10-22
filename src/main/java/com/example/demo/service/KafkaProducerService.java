package com.example.demo.service;

import com.example.demo.model.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, UserMessage> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, UserMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, UserMessage message) {
        kafkaTemplate.send(topic, message).whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Produced: {}", message);
            } else {
                logger.error("Failed to produce: {}", ex.getMessage());
            }
        });
    }
}
