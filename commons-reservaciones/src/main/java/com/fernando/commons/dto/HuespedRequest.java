package com.fernando.commons.dto;

import com.fernando.commons.enums.TipoDocumento;
import jakarta.validation.constraints.*;

public record HuespedRequest(
        @NotBlank(message = "El nombre es necesario")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracterés")
        String nombre,

        @NotBlank(message = "El nombre es necesario")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracterés")
        String apellidoPaterno,

        @NotBlank(message = "El nombre es necesario")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracterés")
        String apellidoMaterno,

        @NotBlank(message = "El email es requerido")
        @Email(message = "Formato de email inválido")
        @Size(min = 1, max = 100, message = "El email debe tener máximo 100 caracteres")
        String email,

        @NotBlank(message = "El telefono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
        String telefono,

        @NotBlank(message = "El documento es necesario")
        @Size(min = 5, max = 50, message = "El documento debe tener entre 5 y 50 caracterés")
        String documento,

        @NotNull(message = "El documento es necesario")
        TipoDocumento tipoDocumento,

        @NotBlank(message = "La nacionalidad es necesario")
        @Size(min = 2, max = 30, message = "La nacionalidad debe tener entre 2 y 20 caracterés")
        String nacionalidad
) {
}
