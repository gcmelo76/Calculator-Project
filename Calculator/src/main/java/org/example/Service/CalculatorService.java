package org.example.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    public BigDecimal add(BigDecimal a, BigDecimal b) {
        logger.info("Starting addition operation. Operands: {} + {}", a, b);
        BigDecimal result = a.add(b);
        logger.debug("Addition operation completed. Result: {}", result);
        return result;
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        logger.info("Starting subtraction operation. Operands: {} - {}", a, b);
        BigDecimal result = a.subtract(b);
        logger.debug("Subtraction operation completed. Result: {}", result);
        return result;
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        logger.info("Starting multiplication operation. Operands: {} * {}", a, b);
        BigDecimal result = a.multiply(b);
        logger.debug("Multiplication operation completed. Result: {}", result);
        return result;
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        logger.info("Starting division operation. Operands: {} / {}", a, b);
        if (b.equals(BigDecimal.ZERO)) {
            logger.error("Division operation failed. Division by zero attempted.");
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        BigDecimal result = a.divide(b, BigDecimal.ROUND_HALF_UP);
        logger.debug("Division operation completed. Result: {}", result);
        return result;
    }
}
