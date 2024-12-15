package org.example.Rest.Kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaProducer {
    private final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.request}")
    private String requestTopic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        logger.info("KafkaProducer initialized with request topic: {}", requestTopic);
    }

    public void sendMessage(String requestId, String operation, BigDecimal a, BigDecimal b) {
        String message = requestId + " " + operation.toUpperCase() + " " + a + " " + b;
        logger.info("Sending message to Kafka topic {}: {}", requestTopic, message);
        kafkaTemplate.send(requestTopic, message);
        logger.debug("Message sent successfully to Kafka");
    }
}