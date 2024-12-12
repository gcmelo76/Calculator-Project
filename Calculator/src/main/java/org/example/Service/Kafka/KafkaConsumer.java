package org.example.Service.Kafka;


import org.example.Service.CalculatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLOutput;

@Service
public class KafkaConsumer {

    private final CalculatorService calculatorService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.response}")
    private String responseTopic;

    public KafkaConsumer(CalculatorService calculatorService, KafkaTemplate<String, String> kafkaTemplate) {
        System.out.println("KafkaConsumer inicializado");
        System.out.println("Bootstrap servers: " + kafkaTemplate.getProducerFactory().getConfigurationProperties().get("bootstrap.servers"));
        this.calculatorService = calculatorService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${kafka.topic.request}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        System.out.println("Received message: " + message);

        try {
            String[] parts = message.split(" ");
            String requestId = parts[0];
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

            System.out.println("Calculation result: " + result);
            kafkaTemplate.send(responseTopic, requestId + " " + result);
            System.out.println("Sent response to Kafka with requestId: " + requestId);

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
