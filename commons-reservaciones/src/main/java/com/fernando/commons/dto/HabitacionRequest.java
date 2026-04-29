package com.fernando.commons.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record HabitacionRequest(

        @NotNull(message = "El numero es requerido")
        @Positive(message = "El número debe ser mayor a 0")
        Integer numero,

        @NotBlank(message = "El tipo es requerido")
        @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
        String tipo,

        @NotNull(message = "El precio es requerido")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @NotNull(message = "La capacidad es requerida")
        @Min(value = 1, message = "La capacidad mínima es 1")
        Integer capacidad,


        @Positive(message = "El idEstadoHabitacion debe ser positivo")
        Long idEstadoHabitacion
) {
}
