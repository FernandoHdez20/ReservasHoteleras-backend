package com.fernando.commons.dto;

import java.math.BigDecimal;

public record DatosHabitacion(
        String numero,
        String tipo,
        String precio,
        String capacidad
) {
}
