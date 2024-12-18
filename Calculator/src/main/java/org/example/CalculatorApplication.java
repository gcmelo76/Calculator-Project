package org.example;

import org.example.Service.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example")
public class CalculatorApplication {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorApplication.class);
    private final CalculatorService calculatorService;

    public CalculatorApplication(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
        logger.info("CalculatorApplication initialized with CalculatorService.");
    }

    public static void main(String[] args) {
        String kafkaUrl = System.getProperty("spring.kafka.bootstrap-servers", "not configured");
        logger.info("Kafka Bootstrap Server URL: {}", kafkaUrl);

        logger.info("Starting CalculatorApplication...");
        SpringApplication.run(CalculatorApplication.class, args);
        logger.info("CalculatorApplication started successfully.");
    }
}
