package mx.gustavo.examen.pruebas.backend.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import mx.gustavo.examen.pruebas.backend.entity.Orden;
import org.springframework.validation.annotation.Validated;

import java.util.List;
/**
 * Define métodos para gestionar operaciones CRUD relacionadas
 * con la entidad Orden y procesa pagos asociados a las órdenes.
 */

@Validated
@Valid
public interface Ordenable {
    /**
     * Procesa el pago para la orden dada y guarda la orden en el repositorio si el pago es exitoso.
     *
     * @param orden la orden que se va a procesar y guardar. No debe ser nula.
     * @return true si el pago se procesó exitosamente y la orden fue guardada, false en caso contrario.
     */
    boolean placeOrden(@NotNull Orden orden);

    /**
     * Recupera una orden específica por su ID.
     *
     * @param id el ID de la orden que se desea recuperar.
     * @return la orden correspondiente al ID proporcionado o null si no se encuentra.
     */
    Orden getOrdenById(int id);

    /**
     * Cancela la orden identificada por su ID, eliminándola del repositorio.
     *
     * @param id el ID de la orden que se desea cancelar
     * @throws IllegalArgumentException si la orden no se encuentra
     */

    void cancelOrden(int id);

    /**
     * Recupera todas las órdenes existentes en el repositorio.
     *
     * @return una lista que contiene todas las órdenes
     */

    List<Orden> listAllOrdenes();
}
