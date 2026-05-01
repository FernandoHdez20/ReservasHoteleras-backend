package com.fernando.commons.dto;

import com.fernando.commons.enums.TipoDocumento;

public record HuespedResponse(
        Long id,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String email,
        String telefono,
        String documento,
        TipoDocumento tipoDocumento,
        String nacionalidad
) {

}
