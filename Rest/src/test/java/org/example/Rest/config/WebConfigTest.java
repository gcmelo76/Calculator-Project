package org.example.Rest.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import static org.mockito.Mockito.*;

class WebConfigTest {

    @Test
    void addCorsMappings_ShouldConfigureCorrectly() {
        // Arrange
        WebConfig webConfig = new WebConfig();
        CorsRegistry registry = mock(CorsRegistry.class);
        when(registry.addMapping(anyString())).thenReturn(new CorsRegistry().addMapping("/**"));

        // Act
        webConfig.addCorsMappings(registry);

        // Assert
        verify(registry).addMapping("/**");
    }
}