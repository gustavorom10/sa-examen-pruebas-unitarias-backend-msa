package mx.gustavo.examen.pruebas.backend.service.implementacion;

import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mx.gustavo.examen.pruebas.backend.entity.Orden;
import mx.gustavo.examen.pruebas.backend.repository.OrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de integración del servicio de Órden
 */
@Slf4j
@Data
@SpringBootTest
class OrdenServiceIntegracionTest {
    @Autowired
    private OrdenService ordenService;

    @MockBean
    private OrdenRepository ordenRepository;

    private Orden ordenValida1Peso;
    private Orden ordenValida2Pesos;
    private Orden ordenValida10Pesos;
    private Orden ordenMinimaPermitida;
    private Orden ordenMaximaPermitida;
    private Orden ordenNula;
    private Orden ordenVacia;
    private Orden ordenInvalidaConIdPeroConMontoNulo;
    private Orden ordenInvalidaConIdCeroConMonto;

    private static final RoundingMode REDONDEO = RoundingMode.HALF_EVEN;
    private static final BigDecimal MONTO_MINIMO_PERMITIDO = new BigDecimal("0.10").setScale(2, REDONDEO);
    private static final BigDecimal MONTO_UNO = BigDecimal.ONE.setScale(2, REDONDEO);
    private static final BigDecimal MONTO_DOS = BigDecimal.TWO.setScale(2, REDONDEO);
    private static final BigDecimal MONTO_DIEZ = BigDecimal.TEN.setScale(2, REDONDEO);
    private static final BigDecimal MONTO_MAXIMO_PERMITIDO = new BigDecimal("9999999999.99").setScale(2, REDONDEO);
    private static final BigDecimal MONTO_MENOR_AL_PERMITIDO = new BigDecimal("0.05").setScale(2, REDONDEO);
    private static final BigDecimal MONTO_MAYOR_AL_PERMITIDO = new BigDecimal("10000000000.00").setScale(2, REDONDEO);

    @BeforeEach
    void setUp() {
        ordenMinimaPermitida = Orden.builder().id(2).amount(MONTO_MINIMO_PERMITIDO).build();
        ordenValida1Peso = Orden.builder().id(1).amount(MONTO_UNO).build();
        ordenValida2Pesos = Orden.builder().id(1).amount(MONTO_DOS).build();
        ordenValida10Pesos = Orden.builder().id(1).amount(MONTO_DIEZ).build();
        ordenMaximaPermitida = Orden.builder().id(1).amount(MONTO_MAXIMO_PERMITIDO).build();
        ordenNula = null;
        ordenVacia = new Orden();
        ordenInvalidaConIdPeroConMontoNulo = Orden.builder().id(2).amount(null).build();
        ordenInvalidaConIdCeroConMonto = Orden.builder().id(0).amount(MONTO_UNO).build();
    }

    @Test
    void givenUnaOrdenConMontoMinimoPermitido_whenPlaceOrden_thenGuardarOrdenValida() {
        assertTrue(ordenService.placeOrden(ordenMinimaPermitida));
    }

    @Test
    void givenUnaOrdenConMontoDe1Peso_whenPlaceOrden_thenGuardarOrdenValida() {
        assertTrue(ordenService.placeOrden(ordenValida1Peso));
    }

    @Test
    void givenUnaOrdenConMontoDe2Pesos_whenPlaceOrden_thenGuardarOrdenValida() {
        assertTrue(ordenService.placeOrden(ordenValida2Pesos));
    }

    @Test
    void givenUnaOrdenConMontoDe10Pesos_whenPlaceOrden_thenGuardarOrdenValida() {
        assertTrue(ordenService.placeOrden(ordenValida10Pesos));
    }

    @Test
    void givenUnaOrdenConMontoMaximoPermitido_whenPlaceOrden_thenGuardarOrdenValida() {
        assertTrue(ordenService.placeOrden(ordenMaximaPermitida));
    }

    @Test
    void givenUnaOrdenConIdCeroYMonto1_whenPlaceOrden_thenGuardarOrdenValida() {
        assertTrue(ordenService.placeOrden(ordenInvalidaConIdCeroConMonto));
    }

    @Test
    void givenUnaOrdenNula_whenPlaceOrden_thenLanzarConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> ordenService.placeOrden(ordenNula));
    }

    @Test
    void givenUnaOrdenVaciaSinIdNiMonto_whenPlaceOrden_thenLanzarConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> ordenService.placeOrden(ordenVacia));
    }

    @Test
    void givenUnaOrdenConId1YMontoNulo_whenPlaceOrden_thenLanzarConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> ordenService.placeOrden(ordenInvalidaConIdPeroConMontoNulo));
    }

    @Test
    void givenUnaOrdenConMontoMenorAlPermitido_whenPlaceOrden_thenLanzarConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> ordenService.placeOrden(new Orden(1, MONTO_MENOR_AL_PERMITIDO)));
    }

    @Test
    void givenUnaOrdenConMontoMayorAlPermitido_whenPlaceOrden_thenLanzarConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> ordenService.placeOrden(new Orden(1, MONTO_MAYOR_AL_PERMITIDO)));
    }

    @Test
    void givenUnaOrdenConIdNegativo_whenCancelOrden_thenLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> ordenService.cancelOrden(-1));
    }

    @Test
    void givenUnaOrdenConIdCero_whenCancelOrden_thenLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> ordenService.cancelOrden(0));
    }
}