package org.example.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    public BigDecimal add(BigDecimal a, BigDecimal b) {
        logger.info("🚀 [ADD] - Operandos recebidos: {} + {}", a, b);
        BigDecimal result = a.add(b);
        logger.debug("🔍 [ADD] - Resultado da soma: {}", result);
        System.out.println("TESTE DE LOG - Console está a funcionar?");
        logger.info("INFO LOG - Teste de logger");
        logger.debug("DEBUG LOG - Teste de logger");
        return result;
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        logger.info("🚀 [SUBTRACT] - Operandos recebidos: {} - {}", a, b);
        BigDecimal result = a.subtract(b);
        logger.debug("🔍 [SUBTRACT] - Resultado da subtração: {}", result);
        return result;
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        logger.info("🚀 [MULTIPLY] - Operandos recebidos: {} * {}", a, b);
        BigDecimal result = a.multiply(b);
        logger.debug("🔍 [MULTIPLY] - Resultado da multiplicação: {}", result);
        return result;
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        logger.info("🚀 [DIVIDE] - Operandos recebidos: {} / {}", a, b);
        if (b.equals(BigDecimal.ZERO)) {
            logger.error("💥 [DIVIDE] - Tentativa de divisão por zero!");
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        BigDecimal result = a.divide(b, BigDecimal.ROUND_HALF_UP);
        logger.debug("🔍 [DIVIDE] - Resultado da divisão: {}", result);
        return result;
    }
}
