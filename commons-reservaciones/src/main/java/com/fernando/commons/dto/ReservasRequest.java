package com.fernando.commons.dto;

import com.fernando.commons.enums.EstadoReserva;

import java.util.Date;

public record ReservasRequest(
        Long idHuesped,
        Long idHabitacion,
        Date fechaEntrada,
        Date fechaSalida,
        EstadoReserva estadoReserva
) { }
