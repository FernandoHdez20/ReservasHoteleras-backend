package com.fernando.commons.dto;

public record HuespedResponse(
        Long id,
        String nombre,
        String email,
        String teléfono,
        String documento,
        String nacionalidad
) {

}
