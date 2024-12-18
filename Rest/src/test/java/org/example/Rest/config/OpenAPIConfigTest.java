package org.example.Rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpenAPIConfigTest {

    @Test
    void baseOpenAPI_ShouldReturnCorrectConfiguration() {
        OpenAPIConfig config = new OpenAPIConfig();

        OpenAPI openAPI = config.baseOpenAPI();

        assertNotNull(openAPI.getInfo());
        assertEquals("Calculator API", openAPI.getInfo().getTitle());
        assertEquals("1.0", openAPI.getInfo().getVersion());
        assertEquals("API de Calculadora com Kafka", openAPI.getInfo().getDescription());
    }
}