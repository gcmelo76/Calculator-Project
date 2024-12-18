package org.example.Rest.Controller;

import org.example.Rest.Kafka.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    @Mock
    private KafkaProducer kafkaProducer;

    private CalculatorController calculatorController;

    @BeforeEach
    void setUp() {
        calculatorController = new CalculatorController(kafkaProducer);
    }

    @Test
    void calculate_ShouldSendMessageToKafka() {
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");

        calculatorController.calculate("add", a, b);
        verify(kafkaProducer).sendMessage(any(), eq("add"), eq(a), eq(b));
    }

    @Test
    void consumeResponse_ShouldCompleteResponseFuture() {
        String requestId = "add-123";
        String message = requestId + " 15.0";

        CompletableFuture<BigDecimal> future = new CompletableFuture<>();
        calculatorController.completeResponse(requestId, new BigDecimal("15.0"));
        calculatorController.consumeResponse(message);
        assertFalse(future.isCompletedExceptionally());
    }
}