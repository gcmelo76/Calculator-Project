package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.Service.CalculatorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example")
public class CalculatorApplication {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorApplication.class);
    private final CalculatorService calculatorService;

    public CalculatorApplication(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public static void main(String[] args) {
        String kafkaUrl = System.getProperty("spring.kafka.bootstrap-servers");
        logger.info("Starting Calculator microservice");
        logger.info("Kafka URL: {}", kafkaUrl);
        SpringApplication.run(CalculatorApplication.class, args);
        logger.info("Calculator microservice started successfully");
    }
}