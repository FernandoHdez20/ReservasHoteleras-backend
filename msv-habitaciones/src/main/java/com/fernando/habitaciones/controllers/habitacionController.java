package com.fernando.habitaciones.controllers;


import com.fernando.commons.controllers.CommonController;
import com.fernando.commons.dto.HabitacionRequest;
import com.fernando.commons.dto.HabitacionResponse;
import com.fernando.habitaciones.services.HabitacionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class HabitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService> {


    public HabitacionController(HabitacionService service) {

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
            @PathVariable Long idEstadoHabitacion
    ) {
        service.actualizarEstadoHabitacion(id, idEstadoHabitacion);
        return ResponseEntity.noContent().build();
    }



}
