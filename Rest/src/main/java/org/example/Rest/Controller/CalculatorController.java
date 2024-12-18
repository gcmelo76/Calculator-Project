package org.example.Rest.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.Rest.Kafka.KafkaProducer;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    private final KafkaProducer kafkaProducer;
    private static final ConcurrentHashMap<String, CompletableFuture<BigDecimal>> responseMap = new ConcurrentHashMap<>();
    String requestId = "";

    public CalculatorController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        logger.info("CalculatorController initialized!");
    }

    @PostMapping("/{operation}")
    public ResponseEntity<?> calculate(@PathVariable String operation, @RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        requestId = UUID.randomUUID().toString();
        MDC.put("request_id", requestId);
        logger.info("Received request to calculate operation: {}, with operands: {} and {}", operation, a, b);

        CompletableFuture<BigDecimal> futureResponse = new CompletableFuture<>();
        responseMap.put(requestId, futureResponse);

        kafkaProducer.sendMessage(requestId, operation, a, b);

        try {
            BigDecimal result = futureResponse.get(5, java.util.concurrent.TimeUnit.SECONDS);
            logger.info("Successfully processed operation: {}, Result: {}", operation, result);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            logger.error("Error processing operation: {}, Request ID: {}", operation, requestId, e);
            return ResponseEntity.status(500).body(Map.of("error", "Operation timed out or failed"));
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "${kafka.topic.response}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeResponse(String message) {
        logger.info("Received Kafka response message: {}", message);
        try {
            String[] parts = message.split(" ");
            String requestId = parts[0];
            BigDecimal result = new BigDecimal(parts[1]);
            completeResponse(requestId, result);
        } catch (Exception e) {
            logger.error("Error processing Kafka response: {}", message, e);
        }
    }

    public static void completeResponse(String requestId, BigDecimal result) {
        logger.info("Completing response for Request ID: {}, with result: {}", requestId, result);
        CompletableFuture<BigDecimal> future = responseMap.remove(requestId);
        if (future != null) {
            future.complete(result);
            logger.info("Future completed successfully for Request ID: {}", requestId);
        } else {
            logger.error("No future found for Request ID: {}", requestId);
        }
    }
}
