package org.example.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculatorService {
    private final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    public BigDecimal add(BigDecimal a, BigDecimal b) {
        logger.info("Performing addition: {} + {}", a, b);
        BigDecimal result = a.add(b);
        logger.debug("Addition result: {}", result);
        return result;
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        logger.info("Performing subtraction: {} - {}", a, b);
        BigDecimal result = a.subtract(b);
        logger.debug("Subtraction result: {}", result);
        return result;
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        logger.info("Performing multiplication: {} * {}", a, b);
        BigDecimal result = a.multiply(b);
        logger.debug("Multiplication result: {}", result);
        return result;
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        logger.info("Performing division: {} / {}", a, b);
        if (b.equals(BigDecimal.ZERO)) {
            logger.error("Attempted division by zero");
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        BigDecimal result = a.divide(b, BigDecimal.ROUND_HALF_UP);
        logger.debug("Division result: {}", result);
        return result;
    }
}