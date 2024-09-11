package mx.gustavo.examen.pruebas.backend.service.implementacion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mx.gustavo.examen.pruebas.backend.entity.Orden;
import mx.gustavo.examen.pruebas.backend.repository.OrdenRepository;
import mx.gustavo.examen.pruebas.backend.service.Ordenable;
import mx.gustavo.examen.pruebas.backend.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Valid
@Data
@Service
public class OrdenService implements Ordenable {

    private OrdenRepository ordenRepository;
    private PaymentService paymentService;

    public OrdenService(OrdenRepository ordenRepository, PaymentService paymentService) {
        this.ordenRepository = ordenRepository;
        this.paymentService = paymentService;
    }

    @Override
    public boolean placeOrden(@NotNull Orden orden) {
        boolean paymentProcessed = paymentService.processPayment(orden.getAmount());
        if (paymentProcessed) {
            ordenRepository.save(orden);
            return true;
        }
        return false;
    }

    @Override
    public Orden getOrdenById(int id) {
        return ordenRepository.findById(id).orElse(null);
    }

    @Override
    public void cancelOrden(int id) {
        Orden orden = ordenRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        ordenRepository.delete(orden);
    }

    @Override
    public List<Orden> listAllOrdenes() {
        return ordenRepository.findAll();
    }
}