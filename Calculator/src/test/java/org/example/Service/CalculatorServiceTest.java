package org.example.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    @DisplayName("Soma dois números positivos")
    void add_WithPositiveNumbers_ShouldReturnSum() {
        // Arrange
        BigDecimal a = new BigDecimal("10");
        BigDecimal b = new BigDecimal("5");

        // Act
        BigDecimal result = calculatorService.add(a, b);

        // Assert
        assertEquals(new BigDecimal("15"), result);
    }

    @Test
    @DisplayName("Subtrai dois números")
    void subtract_ShouldReturnDifference() {
        assertEquals(new BigDecimal("5"),
                calculatorService.subtract(new BigDecimal("10"), new BigDecimal("5")));
    }

    @Test
    @DisplayName("Multiplica dois números")
    void multiply_ShouldReturnProduct() {
        assertEquals(new BigDecimal("50"),
                calculatorService.multiply(new BigDecimal("10"), new BigDecimal("5")));
    }

    @Test
    @DisplayName("Divide dois números")
    void divide_WithValidDivisor_ShouldReturnQuotient() {
        assertEquals(new BigDecimal("2"),
                calculatorService.divide(new BigDecimal("10"), new BigDecimal("5")));
    }

    @Test
    @DisplayName("Divisão por zero deve lançar exceção")
    void divide_ByZero_ShouldThrowException() {
        assertThrows(ArithmeticException.class, () ->
                calculatorService.divide(new BigDecimal("10"), BigDecimal.ZERO));
    }
}