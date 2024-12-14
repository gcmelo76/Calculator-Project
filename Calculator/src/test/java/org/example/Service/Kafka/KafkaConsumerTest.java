package org.example.Service.Kafka;

import org.example.Service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private CalculatorService calculatorService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ProducerFactory<String, String> producerFactory;

    private KafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        when(kafkaTemplate.getProducerFactory()).thenReturn(producerFactory);
        when(producerFactory.getConfigurationProperties()).thenReturn(new HashMap<>());

        kafkaConsumer = new KafkaConsumer(calculatorService, kafkaTemplate);
        ReflectionTestUtils.setField(kafkaConsumer, "responseTopic", "response-topic");
    }

    @Test
    void consume_WithValidAddMessage_ShouldProcessAndSendResponse() {
        // Arrange
        String message = "request123 ADD 10 5";
        when(calculatorService.add(any(), any())).thenReturn(new BigDecimal("15"));

        // Act
        kafkaConsumer.consume(message);

        // Assert
        verify(calculatorService).add(new BigDecimal("10"), new BigDecimal("5"));
        verify(kafkaTemplate).send(eq("response-topic"), eq("request123 15"));
    }

    @Test
    void consume_WithValidSubtractMessage_ShouldProcessAndSendResponse() {
        // Arrange
        String message = "request123 SUBTRACT 10 5";
        when(calculatorService.subtract(any(), any())).thenReturn(new BigDecimal("5"));

        // Act
        kafkaConsumer.consume(message);

        // Assert
        verify(calculatorService).subtract(new BigDecimal("10"), new BigDecimal("5"));
        verify(kafkaTemplate).send(eq("response-topic"), eq("request123 5"));
    }

    @Test
    void consume_WithValidMultiplyMessage_ShouldProcessAndSendResponse() {
        // Arrange
        String message = "request123 MULTIPLY 10 5";
        when(calculatorService.multiply(any(), any())).thenReturn(new BigDecimal("50"));

        // Act
        kafkaConsumer.consume(message);

        // Assert
        verify(calculatorService).multiply(new BigDecimal("10"), new BigDecimal("5"));
        verify(kafkaTemplate).send(eq("response-topic"), eq("request123 50"));
    }

    @Test
    void consume_WithValidDivideMessage_ShouldProcessAndSendResponse() {
        // Arrange
        String message = "request123 DIVIDE 10 5";
        when(calculatorService.divide(any(), any())).thenReturn(new BigDecimal("2"));

        // Act
        kafkaConsumer.consume(message);

        // Assert
        verify(calculatorService).divide(new BigDecimal("10"), new BigDecimal("5"));
        verify(kafkaTemplate).send(eq("response-topic"), eq("request123 2"));
    }
}