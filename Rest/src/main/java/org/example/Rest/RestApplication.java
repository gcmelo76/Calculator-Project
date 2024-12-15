package org.example.Rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example")
public class RestApplication {
    private static final Logger logger = LoggerFactory.getLogger(RestApplication.class);

    public static void main(String[] args) {
        logger.info("Starting RestApplication...");
        SpringApplication.run(RestApplication.class, args);
        logger.info("RestApplication started successfully");
    }
}