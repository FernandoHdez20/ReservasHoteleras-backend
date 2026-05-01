package com.fernando.commons.clients;

import com.fernando.commons.dto.HabitacionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msv-reservas")
public interface ReservasClient {

    @PatchMapping("/reservas/{idReserva}/estado/{idEstado}")
    void actualizarEstadoReserva(
            @PathVariable Long idReserva,
            @PathVariable Long idEstado
    );


}
