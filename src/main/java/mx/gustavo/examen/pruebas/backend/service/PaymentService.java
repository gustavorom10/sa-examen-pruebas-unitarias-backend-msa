package mx.gustavo.examen.pruebas.backend.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
/**
 * Define las operaciones para procesar pagos
 *
 */
@Validated
@Valid
public interface PaymentService {
    /**
     * Procesa el pago con el monto especificado.
     *
     * @param amount el monto a procesar, que debe ser positivo,
     *               con un valor mínimo de 0.10 y con hasta 10 dígitos
     *               enteros y 2 dígitos fraccionarios.
     * @return true si el pago se procesa exitosamente, de lo contrario false.
     */

    boolean processPayment(@NotNull @Positive @DecimalMin("0.10") @Digits(integer = 10, fraction = 2) BigDecimal amount);
}
