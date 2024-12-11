package org.example.Rest.Controller;

import org.example.Rest.Kafka.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final KafkaProducer kafkaProducer;
    private static final ConcurrentHashMap<String, CompletableFuture<BigDecimal>> responseMap = new ConcurrentHashMap<>();

    public CalculatorController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/{operation}")
    public ResponseEntity<CompletableFuture<BigDecimal>> calculate(
            @PathVariable String operation,
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b
    ) {
        String requestId = operation + "-" + System.currentTimeMillis();
        CompletableFuture<BigDecimal> futureResponse = new CompletableFuture<>();
        responseMap.put(requestId, futureResponse);

        String message = requestId + " " + operation.toUpperCase() + " " + a + " " + b;
        kafkaProducer.sendMessage(message);

        return ResponseEntity.ok(futureResponse);
    }

    public static void completeResponse(String requestId, BigDecimal result) {
        CompletableFuture<BigDecimal> future = responseMap.remove(requestId);
        if (future != null) {
            future.complete(result);
        }
    }
}
