package mx.gustavo.examen.pruebas.backend.repository;

import mx.gustavo.examen.pruebas.backend.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Proporciona operaciones CRUD para la entidad Orden.
 * Extiende JpaRepository para aprovechar las funcionalidades de Spring Data JPA.
 *
 * Puede ser utilizada como una dependencia en clases de servicio como OrdenService,
 * donde se encapsulan la lógica de negocio y las operaciones de acceso a datos, asegurando
 * un diseño más modular y fácil de probar.
 */
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {
}
