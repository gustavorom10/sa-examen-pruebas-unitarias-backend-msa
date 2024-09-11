package mx.gustavo.examen.pruebas.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Representa una orden de compra que el cliente hace en nuestra tienda
 * Cambié el nombre de la tabla Order por Ordenable porque Order es una palabra reservada de SQL y causará conflictos.
 * No se recomienda renombrar a Orders porque la convención es que los nombres de tablas deben ser en singular.
 * Otra solución sin tener que renombrar la tabla es usar la anotación @Table(name='order') y encerrar la palabra order entre comillas simples,
 * comillas dobles, tildes invertidas o corchetes según el motor de BD que se vaya a utilizar; pero tampoco es la práctica más limpia.
 * Mejores soluciones: renombrar a Ordenable, customerOrder o purchaseOrder, prefiero el español porque somos latinos.
 */

@NotNull
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Orden {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    @Positive
    @DecimalMin(value = "0.10")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;
}
