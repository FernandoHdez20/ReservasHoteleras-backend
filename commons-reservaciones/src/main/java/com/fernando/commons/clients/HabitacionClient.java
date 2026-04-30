package com.fernando.commons.clients;

import com.fernando.commons.dto.HabitacionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "msv-habitaciones")
public interface HabitacionClient {

    @GetMapping("/habitaciones/{id}")
    HabitacionResponse obtenerHabitacion(@PathVariable Long id);

    @PutMapping("/habitaciones/{id}/estado/{estado}")
    void actualizarEstadoHabitacion(
            @PathVariable Long id,
            @PathVariable Long estado
    );
}

