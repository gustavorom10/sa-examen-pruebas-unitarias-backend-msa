package mx.gustavo.examen.pruebas.backend.service.implementacion;

import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mx.gustavo.examen.pruebas.backend.entity.Orden;
import mx.gustavo.examen.pruebas.backend.repository.OrdenRepository;
import mx.gustavo.examen.pruebas.backend.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * Prueba unitariamente el comportamiento de OrdenService
 * Se colocó 1 método con redacción estilo Mockito
 * Se colocó 1 método con estilo MockitoBDD (Mockito Behavior Driven Development)
 * El resto de métodos están codificados con MockitoBDD con estilo de redacción resumido para teclear menos, pero
 * manteniendo la convención
 */
@Validated
@Valid
@Data
@ValidateOnExecution
@ExtendWith(MockitoExtension.class)
class OrdenServiceTest {
    @Mock
    private OrdenRepository ordenRepository;
    @Mock
    PaymentService paymentService;
    @InjectMocks
    OrdenService ordenService;
    Orden ordenValida1Peso;
    Orden ordenMinimaPermitida;
    Orden ordenMaximaPermitida;
    Orden ordenNula;
    Orden ordenVacia;
    Orden ordenInvalidaConIdPeroConMontoNulo;
    Orden ordenValidaConIdCeroConMonto;
    BigDecimal montoNulo;
    BigDecimal montoMinimoPermitido;
    BigDecimal montoUno;
    BigDecimal montoDos;
    BigDecimal montoDiez;
    BigDecimal montoMaximoPermitido;
    private BigDecimal montoMenorAlPermitido;
    private BigDecimal montoMayorAlPermitido;

    private static final int ESCALA = 2;
    private static final RoundingMode REDONDEO = RoundingMode.HALF_EVEN;

    @BeforeEach
    void setUp() {
        montoNulo = null;
        montoMayorAlPermitido = inicializarBigDecimal("10000000000.00");
        montoDos = inicializarBigDecimal(BigDecimal.TWO);
        montoDiez = inicializarBigDecimal(BigDecimal.TEN);
        montoMaximoPermitido = inicializarBigDecimal("9999999999.99");
        montoMinimoPermitido = inicializarBigDecimal("0.10");
        montoUno = inicializarBigDecimal(BigDecimal.ONE);
        
        ordenNula = null;
        ordenVacia = new Orden();
        ordenInvalidaConIdPeroConMontoNulo = Orden.builder().id(2).amount(montoNulo).build();
        ordenValidaConIdCeroConMonto = Orden.builder().id(0).amount(montoUno).build();
        ordenMinimaPermitida = Orden.builder().id(2).amount(montoMinimoPermitido).build();
        ordenMaximaPermitida = Orden.builder().id(1).amount(montoMaximoPermitido).build();
        ordenValida1Peso = Orden.builder().id(1).amount(montoUno).build();
    }

    /**
     * No es un test.
     * Es para iniciar los montos en formato monetario: escala de 2 decimales y modo de redondeo bancario 
     * @param valor monto como cadena de texto
     * @return Objeto Bigdecimal que represent el monto recibido en formato monetario
     */
    private BigDecimal inicializarBigDecimal(String valor) {
        return new BigDecimal(valor).setScale(ESCALA, REDONDEO);
    }

    /**
     * No es un test.
     * Es para iniciar los montos en formato monetario: escala de 2 decimales y modo de redondeo bancario 
     * @param valor monto como objeto Bigdecimal sin escala ni modo de redondeo
     * @return Objeto Bigdecimal que represent el monto recibido en formato monetario
     */
    private BigDecimal inicializarBigDecimal(BigDecimal valor) {
        return valor.setScale(ESCALA, REDONDEO);
    }

    /**
     * Con estilo de Mockito
     */
    @Test
    void givenUnaOrdenValidaConMontoDe1Peso_whenPlaceOrden_thenGuardarOrdenValidaMockito() {
        when(paymentService.processPayment(montoUno)).thenReturn(true);
        boolean ordenGuardada = ordenService.placeOrden(ordenValida1Peso);
        verify(ordenRepository).save(ordenValida1Peso);
        assertTrue(ordenGuardada);
    }

    /**
     * Con estilo de MockitoBDD (Mockito Behavior Driven Development)
     */
    @Test
    void givenUnaOrdenValidaConMontoDe1Peso_whenPlaceOrden_thenGuardarOrdenValidaMockitoBDD() {
        given(paymentService.processPayment(montoUno)).willReturn(true);
        boolean ordenGuardada = ordenService.placeOrden(ordenValida1Peso);
        assertTrue(ordenGuardada);
    }

    /**
     * Con estilo de redacción de MockitoBDD resumido o contraído, para teclear menos
     */
    @Test
    void givenUnaOrdenConMontoMinimoPermitido_whenPlaceOrden_thenGuardarOrdenValida() {
        montoMinimoPermitido = new BigDecimal("0.10").setScale(2, RoundingMode.HALF_EVEN);
        ordenMinimaPermitida = new Orden(2, montoMinimoPermitido);
        given(paymentService.processPayment(montoMinimoPermitido)).willReturn(true);
        assertTrue(ordenService.placeOrden(ordenMinimaPermitida));
    }

    @Test
    void givenUnaOrdenConMontoMaximoPermitido_whenPlaceOrden_thenGuardarOrdenValida() {
        given(paymentService.processPayment(montoMaximoPermitido)).willReturn(true);
        assertTrue(ordenService.placeOrden(ordenMaximaPermitida));
    }

    /**
     * El ID de una entidad Orden es autogenerado o autoincremental comúnmente en cualquier app y el primer ID que
     * se va a generar es el número 1. Por lo tanto, lo asumimos como tal en esta prueba.
     */
    @Test
    void givenUnaOrdenConIdCeroYMonto1_whenPlaceOrden_thenGuardarOrdenValida() {
        given(paymentService.processPayment(montoUno)).willReturn(true);
        assertTrue(ordenService.placeOrden(ordenValidaConIdCeroConMonto));
    }

    @Test
    void givenUnaOrdenNula_whenPlaceOrden_thenLanzarNullPointerException() {
        assertThrows(NullPointerException.class, () -> ordenService.placeOrden(ordenNula));
    }

    @Test
    void givenUnaOrdenVaciaSinIdNiMonto_whenPlaceOrden_thenLanzarConstraintViolationException() {
        given(paymentService.processPayment(montoNulo)).willReturn(false);
        assertFalse(ordenService.placeOrden(ordenVacia));
    }

    @Test
    void givenUnaOrdenConId1YMontoNulo_whenPlaceOrden_thenLanzarConstraintViolationException() {
        given(paymentService.processPayment(montoNulo)).willReturn(false);
        assertFalse(ordenService.placeOrden(ordenInvalidaConIdPeroConMontoNulo));
    }

    @Test
    void givenUnaOrdenConMontoMenorAlPermitido_whenPlaceOrden_thenLanzarConstraintViolationException() {
        montoMenorAlPermitido = new BigDecimal("0.05").setScale(2, RoundingMode.HALF_EVEN);
        given(paymentService.processPayment(montoMenorAlPermitido)).willReturn(false);
        assertFalse(ordenService.placeOrden(new Orden(1, montoMenorAlPermitido)));
    }

    @Test
    void ginvenUnaOrdenConMontoMayorAlPermitido_whenPlaceOrden_thenLanzarConstraintViolationException() {
        given(paymentService.processPayment(montoMayorAlPermitido)).willReturn(false);
        assertFalse(ordenService.placeOrden(new Orden(1, montoMayorAlPermitido)));
    }


    @Test
    void givenUnaOrdenExistenteEnBD_getOrdenById_thenObtenerUnaOrdenNoNula() {
        given(ordenRepository.findById(1)).willReturn(Optional.of(ordenValida1Peso));
        Orden ordenActual = ordenService.getOrdenById(1);
        assertThat(ordenActual).isNotNull();
    }

    @Test
    void givenUnaOrdenExistenteEnBD_getOrdenById_thenObtenerLaOrdenBuscada() {
        given(ordenRepository.findById(1)).willReturn(Optional.of(ordenValida1Peso));
        Orden ordenActual = ordenService.getOrdenById(1);
        assertEquals(ordenValida1Peso, ordenActual);
    }

    @Test
    void givenUnaOrdenInexistenteEnBD_getOrdenById_thenObtenerUnaOrdenNula() {
        given(ordenRepository.findById(1)).willReturn(Optional.empty());
        Orden ordenActual = ordenService.getOrdenById(1);
        assertNull(ordenActual);
    }

    @Test
    void givenUnaOrdenInexistenteEnBD_cancelOrden_thenLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> ordenService.cancelOrden(1));

    }

    @Test
    void givenUnaOrdenConIdNegativo_whenCancelOrden_thenLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> ordenService.cancelOrden(-1));
    }

    @Test
    void givenUnaOrdenConIdCero_whenCancelOrden_thenLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> ordenService.cancelOrden(0));
    }

    @Test
    void givenUnOrdenIdValido_whenCancelOrden_thenCancelarOrden() {
        given(ordenRepository.findById(1)).willReturn(Optional.of(ordenValida1Peso));
        willDoNothing().given(ordenRepository).delete(ordenValida1Peso);
        ordenService.cancelOrden(1);
        verify(ordenRepository).delete(ordenValida1Peso);
    }

    @Test
    void givenUnaTablaVaciaDeOrdenes_listAllOrdenes_thenRetornarListaVacia() {
        given(ordenRepository.findAll()).willReturn(emptyList());
        List<Orden> ordenesActual = ordenService.listAllOrdenes();
        assertTrue(ordenesActual.isEmpty());
    }

    @Test
    void givenUnaTablaConVariasOrdenes_listAllOrdenes_thenRetornarListarTodasLasOrdenes() {
        List<Orden> ordenesEsperadas = List.of(ordenValida1Peso, ordenMinimaPermitida,
                ordenMaximaPermitida);
        given(ordenRepository.findAll()).willReturn(ordenesEsperadas);
        assertEquals(ordenesEsperadas, ordenService.listAllOrdenes());
    }
}