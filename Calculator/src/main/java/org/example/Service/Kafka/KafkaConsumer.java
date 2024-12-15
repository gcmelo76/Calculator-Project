package org.example.Service.Kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.Service.CalculatorService;
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
        logger.info("Initializing KafkaConsumer");
        logger.info("Bootstrap servers: {}",
                kafkaTemplate.getProducerFactory().getConfigurationProperties().get("bootstrap.servers"));
        this.calculatorService = calculatorService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${kafka.topic.request}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        logger.info("Received message: {}", message);

        try {
            String[] parts = message.split(" ");
            String requestId = parts[0];
            String operation = parts[1];
            BigDecimal a = new BigDecimal(parts[2]);
            BigDecimal b = new BigDecimal(parts[3]);

            logger.debug("Processing request ID: {}, Operation: {}, Values: {} {}", requestId, operation, a, b);

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
                    logger.error("Invalid operation received: {}", operation);
                    throw new IllegalArgumentException("Invalid operation: " + operation);
            }

            logger.info("Calculation result for request {}: {}", requestId, result);
            kafkaTemplate.send(responseTopic, requestId + " " + result);
            logger.info("Response sent to Kafka topic {} with requestId: {}", responseTopic, requestId);

        } catch (Exception e) {
            logger.error("Error processing message: {}. Error: {}", message, e.getMessage(), e);
        }
    }
}