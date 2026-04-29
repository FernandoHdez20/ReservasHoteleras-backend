package com.fernando.habitaciones.mappers;

import com.fernando.commons.dto.HabitacionRequest;
import com.fernando.commons.dto.HabitacionResponse;
import com.fernando.commons.enums.EstadoHabitacion;
import com.fernando.commons.enums.EstadoRegistro;
import com.fernando.commons.mappers.CommonMapper;
import com.fernando.habitaciones.entities.Habitacion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class HabitacionMapper implements CommonMapper<HabitacionRequest, HabitacionResponse, Habitacion> {

    @Override
    public Habitacion requestAEntidad(HabitacionRequest request) {
        if (request == null) return null;

        return Habitacion.builder()
                .numero(request.numero())
                .tipo(request.tipo())
                .precio(request.precio())
                .capacidad(request.capacidad())
                .estadoHabitacion(EstadoHabitacion.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public HabitacionResponse entidadAResponse(Habitacion entidad) {
        if (entidad == null) return null;


        return new HabitacionResponse(
                entidad.getId(),
                entidad.getNumero(),
                entidad.getTipo(),
                entidad.getPrecio(),
                entidad.getCapacidad(),
                entidad.getEstadoHabitacion().getDescripcion()
        );
    }
}
