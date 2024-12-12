package org.example.Rest.Kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.request}")
    private String requestTopic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String requestId, String operation, BigDecimal a, BigDecimal b) {
        String message = requestId + " " + operation.toUpperCase() + " " + a + " " + b;
        kafkaTemplate.send(requestTopic, message);
        System.out.println("Message sent to Kafka: " + message);
    }

}
