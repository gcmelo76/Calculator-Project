package org.example;

import org.example.Service.CalculatorService;
import org.example.Service.Kafka.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = CalculatorApplication.class)
@EmbeddedKafka(partitions = 1, topics = {"calculator-requests", "calculator-responses"})
@TestPropertySource(properties = {
        "spring.main.banner-mode=off",
        "logging.level.root=OFF",
        "spring.kafka.consumer.auto-offset-reset=earliest",
        "spring.kafka.consumer.group-id=test-group",
        "kafka.topic.request=test-topic",
        "kafka.topic.response=test-response-topic",
        "spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer"
})
public class CalculatorApplicationTest {

    @MockBean
    private CalculatorService calculatorService;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private KafkaConsumer kafkaConsumer;

    @Test
    void contextLoads() {
    }
}