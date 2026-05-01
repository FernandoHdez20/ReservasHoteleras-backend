package com.fernando.commons.clients;

import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msv-reservas")
public interface ReservasClient {

    @GetMapping("/id-huesped/{idHuesped}/reservacion-asignada")
    Void huespedTieneReservacionesAsignada(
            @PathVariable @Positive(message = "El idHuesped debe ser positivo") Long idHuesped);

    @GetMapping("/id-habitacion/{idHabitacion}/reservacion-asignada")
    Void habitacionTieneReservacionesAsignadas(
            @PathVariable @Positive(message = "El idHabitacion debe ser positivo") Long idHabitacion);


}
