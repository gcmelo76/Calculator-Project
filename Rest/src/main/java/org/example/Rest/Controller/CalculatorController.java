package org.example.Rest.Controller;

import org.example.Rest.Kafka.KafkaProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final KafkaProducer kafkaProducer;
    private static final ConcurrentHashMap<String, CompletableFuture<BigDecimal>> responseMap = new ConcurrentHashMap<>();

    public CalculatorController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        System.out.println("CalculatorController initialized!");
    }

    @PostMapping("/{operation}")
    public ResponseEntity<?> calculate(
            @PathVariable String operation,
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        String requestId = operation + "-" + System.currentTimeMillis();


        CompletableFuture<BigDecimal> futureResponse = new CompletableFuture<>();
        responseMap.put(requestId, futureResponse);

        kafkaProducer.sendMessage(requestId, operation, a, b);
        System.out.println("Message sent to Kafka with requestId: " + requestId);

        try {
            BigDecimal result = futureResponse.get(5, java.util.concurrent.TimeUnit.SECONDS);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            System.err.println("Error while waiting for Kafka response: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Operation timed out or failed"));
        }
    }

    @KafkaListener(topics = "${kafka.topic.response}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeResponse(String message) {
        System.out.println("Received response message: " + message);

        try {
            String[] parts = message.split(" ");
            String requestId = parts[0];
            BigDecimal result = new BigDecimal(parts[1]);


            completeResponse(requestId, result);

        } catch (Exception e) {
            System.err.println("Error processing response message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void completeResponse(String requestId, BigDecimal result) {
        System.out.println("Completing response for: " + requestId + " with result: " + result);
        CompletableFuture<BigDecimal> future = responseMap.remove(requestId);
        if (future != null) {
            future.complete(result);
            System.out.println("Future completed successfully for requestId: " + requestId);
        } else {
            System.err.println("No future found for requestId: " + requestId);
        }
    }
}
