package com.fernando.commons.clients;

import com.fernando.commons.dto.HabitacionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "msv-habitaciones")
public interface HabitacionClient {

    @GetMapping("/{id}")
    HabitacionResponse obtenerHabitacionPorId(@PathVariable Long id);

    @PutMapping("/id-habitacion/{id}")
    HabitacionResponse obtenerHabitacionSinEstado(
            @PathVariable Long id
    );

    @PutMapping("/{id}/estado/{estado}")
    void actualizarEstadoHabitacion(
            @PathVariable Long id,
            @PathVariable Long estado
    );

    @PutMapping("/{id}/liberar")
    void liberarHabitacion(@PathVariable Long id);

}

