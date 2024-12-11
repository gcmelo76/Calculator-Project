package org.example.Service;

import org.example.Service.CalculatorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class ServiceApplication implements CommandLineRunner {

    private final CalculatorService calculatorService;

    public ServiceApplication(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Testing CalculatorService...");
        System.out.println("2 + 3 = " + calculatorService.add(new BigDecimal("2"), new BigDecimal("3")));
        System.out.println("5 - 3 = " + calculatorService.subtract(new BigDecimal("5"), new BigDecimal("3")));
        System.out.println("2 * 3 = " + calculatorService.multiply(new BigDecimal("2"), new BigDecimal("3")));
        System.out.println("6 / 2 = " + calculatorService.divide(new BigDecimal("6"), new BigDecimal("2")));
    }
}
