package mx.gustavo.examen.pruebas.backend.service.implementacion;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mx.gustavo.examen.pruebas.backend.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@Slf4j
@Data
@Service
public class PagoService implements PaymentService {
    @Override
    public boolean processPayment(@NotNull @Positive @DecimalMin("0.10") @Digits(integer = 10, fraction = 2) BigDecimal amount) {
        // cosas para procesar el pago
        return true;
    }
}
