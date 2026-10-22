package com.example.demo.controller;

import com.example.demo.model.UserMessage;
import com.example.demo.service.KafkaConsumerService;
import com.example.demo.service.KafkaProducerService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducerService producerService;
    private final KafkaConsumerService consumerService;

    public KafkaController(KafkaProducerService producerService, KafkaConsumerService consumerService) {
        this.producerService = producerService;
        this.consumerService = consumerService;
    }

    @PostMapping("/publish")
    public String publish(@RequestParam("message") String message) {
        UserMessage userMessage = new UserMessage(UUID.randomUUID().toString(), message, System.currentTimeMillis());
        producerService.sendMessage("my-topic", userMessage);
        return "Message sent to Kafka topic 'my-topic': " + userMessage;
    }

    @GetMapping("/messages")
    public List<UserMessage> getMessages() {
        return consumerService.getConsumedMessages();
    }

    @GetMapping("/messages/dlt")
    public List<UserMessage> getDltMessages() {
        return consumerService.getDltMessages();
    }
}
