package org.example.Rest.Controller;

import org.example.Kafka.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final KafkaProducer kafkaProducer;

    public CalculatorController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/{operation}")
    public ResponseEntity<String> calculate(
            @PathVariable String operation,
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        String message = operation.toUpperCase() + " " + a + " " + b;
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to Kafka: " + message);
    }
}
