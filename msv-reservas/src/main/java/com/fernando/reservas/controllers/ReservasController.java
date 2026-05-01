package com.fernando.reservas.controllers;

import com.fernando.commons.controllers.CommonController;
import com.fernando.commons.dto.ReservasRequest;
import com.fernando.commons.dto.ReservasResponse;
import com.fernando.reservas.services.ReservasService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ReservasController extends CommonController<ReservasRequest, ReservasResponse, ReservasService> {

    public ReservasController(ReservasService service) {

        super(service);
    }

    @PatchMapping("/{idReservacion}/estado/{idEstadoReservacion}")
    public ResponseEntity<Void> actualizarEstadoReservacion(
            @PathVariable @Positive(message = "El idReservacion debe ser positivo") Long idReservacion,
            @PathVariable @Positive(message = "El idEstadoReservacion debe ser positivo") Long idEstadoReservacion){
        service.actualizarEstadoReservacion(idReservacion,idEstadoReservacion);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id-habitacion/{idHabitacion}/reservacion-asignada")
    public ResponseEntity<Void> habitacionTieneReservacionesAsignadas(
            @PathVariable @Positive (message = "El idHabitacion debe ser positivo") Long idHabitacion){
        service.habitacionTieneReservacionAsignadas(idHabitacion);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id-huesped/{idHuesped}/reservacion-asignada")
    public ResponseEntity<Void> huespedTieneReservacionesAsignada(
            @PathVariable @Positive (message = "El idHuesped debe ser positivo") Long idHuesped){
        service.huespedTieneReservacionesAsignadas(idHuesped);
        return ResponseEntity.noContent().build();
    }
}
