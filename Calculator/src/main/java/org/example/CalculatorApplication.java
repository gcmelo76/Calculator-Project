package org.example;

import org.example.Service.CalculatorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication(scanBasePackages = "org.example")
public class CalculatorApplication {

    private final CalculatorService calculatorService;

    public CalculatorApplication(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public static void main(String[] args) {
        System.out.println("URL Kafka: " + System.getProperty("spring.kafka.bootstrap-servers"));
        SpringApplication.run(CalculatorApplication.class, args);
    }
}
