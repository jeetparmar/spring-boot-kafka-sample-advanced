package com.example.demo.service;

import com.example.demo.model.UserMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Backoff;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final List<UserMessage> consumedMessages = new CopyOnWriteArrayList<>();
    private final List<UserMessage> dltMessages = new CopyOnWriteArrayList<>();

    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen(UserMessage message) {
        logger.info("Consumed: {}", message);

        if (message.getContent() != null && message.getContent().contains("fail")) {
            logger.error("Simulating failure for message: {}", message.getId());
            throw new RuntimeException("Simulated processing failure");
        }

        consumedMessages.add(message);
    }

    @DltHandler
    public void handleDlt(UserMessage message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        logger.info("Message sent to DLT from topic {}: {}", topic, message);
        dltMessages.add(message);
    }

    public List<UserMessage> getConsumedMessages() {
        return new ArrayList<>(consumedMessages);
    }

    public List<UserMessage> getDltMessages() {
        return new ArrayList<>(dltMessages);
    }

    public void clearMessages() {
        consumedMessages.clear();
        dltMessages.clear();
    }
}
