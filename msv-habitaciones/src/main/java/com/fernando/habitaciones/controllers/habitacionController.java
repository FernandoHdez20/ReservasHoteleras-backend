package com.fernando.habitaciones.controllers;


import com.fernando.commons.controllers.CommonController;
import com.fernando.commons.dto.HabitacionRequest;
import com.fernando.commons.dto.HabitacionResponse;
import com.fernando.habitaciones.services.HabitacionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class habitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService> {


    public habitacionController(HabitacionService service) {

        super(service);
    }

    @GetMapping("/idHabitacion/{id}")
    public ResponseEntity<HabitacionResponse> obtenerPorIdHabitacionSinEstado(
            @PathVariable
            @Positive(message = "El ID debe ser positivo")
            Long id
    ) {
        return ResponseEntity.ok(service.obtenerPorIdHabitacionSinEstado(id));
    }

    @PutMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoHabitacion(
            @PathVariable Long id,
            @PathVariable Long idEstado
    ) {
        service.actualizarEstadoHabitacion(id, idEstado);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/liberar")
    public ResponseEntity<Void> liberarHabitacion(@PathVariable Long id) {
        service.liberarHabitacion(id);
        return ResponseEntity.noContent().build();
    }

}
