package com.fernando.auth.dto;

public record ErrorResponse(
        int codigo,
        String mensaje
) { }

