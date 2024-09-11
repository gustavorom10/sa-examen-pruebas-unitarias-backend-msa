package mx.gustavo.examen.pruebas.backend.service.implementacion;

import jakarta.validation.ConstraintViolationException;
import mx.gustavo.examen.pruebas.backend.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PagoServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void givenMontoValido_whenProcessPayment_thenSuccess() {
        BigDecimal monto = new BigDecimal("10.00");
        boolean result = paymentService.processPayment(monto);
        assertTrue(result, "Se esperaba que el método processPayment devolviera verdadero para montos válidos");
    }

    @Test
    public void givenMontoNegativo_whenProcessPayment_thenConstraintViolation() {
        BigDecimal monto = new BigDecimal("-10.00");

        Exception excepcion = assertThrows(ConstraintViolationException.class, () -> paymentService.processPayment(monto));

        assertTrue(excepcion.getMessage().contains("debe ser mayor que 0"), "Se esperaba que el método processPayment fallara para un monto negativo");
    }

    @Test
    public void givenMontoCero_whenProcessPayment_thenConstraintViolation() {
        BigDecimal monto = new BigDecimal("0.00");

        Exception excepcion = assertThrows(ConstraintViolationException.class, () -> paymentService.processPayment(monto));

        assertTrue(excepcion.getMessage().contains("debe ser mayor que 0"), "Se esperaba que el método processPayment fallara para un monto cero");
    }
}