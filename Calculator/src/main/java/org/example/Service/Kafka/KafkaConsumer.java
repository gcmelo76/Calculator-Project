package org.example.Service.Kafka;

import org.example.Rest.Controller.CalculatorController;
import org.example.Service.CalculatorService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaConsumer {

    private final CalculatorService calculatorService;

    public KafkaConsumer(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }
    @KafkaListener(topics = "${kafka.topic.request}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        System.out.println("Received message: " + message);

        try {
            String[] parts = message.split(" ");
            String requestId = parts[0]; // Extrai o ID da mensagem
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

            CalculatorController.completeResponse(requestId, result);

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
