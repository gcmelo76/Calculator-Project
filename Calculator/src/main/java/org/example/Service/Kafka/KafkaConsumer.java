package org.example.Service.Kafka;

import org.example.Service.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final CalculatorService calculatorService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.response}")
    private String responseTopic;

    public KafkaConsumer(CalculatorService calculatorService, KafkaTemplate<String, String> kafkaTemplate) {
        this.calculatorService = calculatorService;
        this.kafkaTemplate = kafkaTemplate;

        logger.info("KafkaConsumer initialized successfully.");
        logger.debug("Bootstrap servers: {}", kafkaTemplate.getProducerFactory().getConfigurationProperties().get("bootstrap.servers"));
    }

    @KafkaListener(topics = "${kafka.topic.request}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        logger.info("Received message from Kafka: {}", message);

        try {
            String[] parts = message.split(" ");
            String requestId = parts[0];
            MDC.put("request_id", requestId);
            String operation = parts[1];
            BigDecimal a = new BigDecimal(parts[2]);
            BigDecimal b = new BigDecimal(parts[3]);

            BigDecimal result;
            switch (operation.toUpperCase()) {
                case "ADD":
                    result = calculatorService.add(a, b);
                    break;
                case "SUBTRACT":
                    result = calculatorService.subtract(a, b);
                    break;
                case "MULTIPLY":
                    result = calculatorService.multiply(a, b);
                    break;
                case "DIVIDE":
                    result = calculatorService.divide(a, b);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation: " + operation);
            }

            logger.info("Calculation result for: {}", result);
            kafkaTemplate.send(responseTopic, requestId + " " + result);
            logger.info("Sent response to Kafka. Result: {}", result);

        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
        }finally{
            MDC.clear();
        }
    }
}