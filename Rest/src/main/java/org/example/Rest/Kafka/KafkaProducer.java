package org.example.Rest.Kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.request}")
    private String requestTopic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        logger.info("KafkaProducer initialized successfully.");
    }

    public void sendMessage(String requestId, String operation, BigDecimal a, BigDecimal b) {
        logger.info("Preparing to send message. Request ID: {}, Operation: {}, Operand A: {}, Operand B: {}", requestId, operation, a, b);
        String message = requestId + " " + operation.toUpperCase() + " " + a + " " + b;
        kafkaTemplate.send(requestTopic, message);
        logger.info("Message sent to Kafka. Topic: {}, Message: {}", requestTopic, message);
    }
}
