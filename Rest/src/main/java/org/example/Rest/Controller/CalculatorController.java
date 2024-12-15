package org.example.Rest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag(name = "Calculator", description = "API de Calculadora com processamento assíncrono via Kafka")
public class CalculatorController {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    private final KafkaProducer kafkaProducer;
    private static final ConcurrentHashMap<String, CompletableFuture<BigDecimal>> responseMap = new ConcurrentHashMap<>();

    public CalculatorController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        logger.info("CalculatorController initialized");
    }

    @Operation(summary = "Realizar operação matemática",
            description = "Executa uma operação matemática básica (soma, subtração, multiplicação, divisão) usando processamento assíncrono via Kafka")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Erro ao processar a operação",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/{operation}")
    public ResponseEntity<?> calculate(
            @Parameter(description = "Operação matemática (add, subtract, multiply, divide)",
                    example = "add")
            @PathVariable String operation,

            @Parameter(description = "Primeiro número",
                    example = "10.5")
            @RequestParam BigDecimal a,

            @Parameter(description = "Segundo número",
                    example = "5.2")
            @RequestParam BigDecimal b
    ) {
        String requestId = operation + "-" + System.currentTimeMillis();
        logger.info("Received calculation request - Operation: {}, Values: {} {}, RequestId: {}",
                operation, a, b, requestId);

        CompletableFuture<BigDecimal> futureResponse = new CompletableFuture<>();
        responseMap.put(requestId, futureResponse);

        kafkaProducer.sendMessage(requestId, operation, a, b);
        logger.debug("Request sent to Kafka, waiting for response - RequestId: {}", requestId);

        try {
            BigDecimal result = futureResponse.get(5, java.util.concurrent.TimeUnit.SECONDS);
            logger.info("Received result for RequestId {}: {}", requestId, result);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            logger.error("Error processing request {}: {}", requestId, e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Operation timed out or failed"));
        }
    }

    @KafkaListener(topics = "${kafka.topic.response}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeResponse(String message) {
        logger.info("Received response from Kafka: {}", message);

        try {
            String[] parts = message.split(" ");
            String requestId = parts[0];
            BigDecimal result = new BigDecimal(parts[1]);

            logger.debug("Processing response for RequestId: {}", requestId);
            completeResponse(requestId, result);

        } catch (Exception e) {
            logger.error("Error processing Kafka response: {}", e.getMessage(), e);
        }
    }

    public static void completeResponse(String requestId, BigDecimal result) {
        logger.info("Completing response for RequestId: {} with result: {}", requestId, result);
        CompletableFuture<BigDecimal> future = responseMap.remove(requestId);
        if (future != null) {
            future.complete(result);
            logger.debug("Future completed successfully for RequestId: {}", requestId);
        } else {
            logger.error("No future found for RequestId: {}", requestId);
        }
    }
}