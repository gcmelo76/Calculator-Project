package org.example.Rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example")
public class RestApplication {

    public static void main(String[] args) {
        System.out.println("Starting RestApplication...");
        SpringApplication.run(RestApplication.class, args);
    }
}
