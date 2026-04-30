package com.fernando.commons.dto;

import com.fernando.commons.enums.EstadoReserva;

import java.util.Date;

public record ReservasResponse(
        Long id,
        Long idHuesped,
        Long idHabitacion,
        Date fechaEntrada,
        Date fechaSalida,
        String estadoReserva
) {
}
