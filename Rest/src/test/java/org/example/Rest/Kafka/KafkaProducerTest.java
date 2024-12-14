package org.example.Rest.Kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        kafkaProducer = new KafkaProducer(kafkaTemplate);
        ReflectionTestUtils.setField(kafkaProducer, "requestTopic", "test-topic");
    }

    @Test
    void sendMessage_ShouldSendFormattedMessageToKafka() {
        // Arrange
        String requestId = "123";
        String operation = "ADD";
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");
        String expectedMessage = "123 ADD 10 5";

        // Act
        kafkaProducer.sendMessage(requestId, operation, a, b);

        // Assert
        verify(kafkaTemplate).send(eq("test-topic"), eq(expectedMessage));
    }
}