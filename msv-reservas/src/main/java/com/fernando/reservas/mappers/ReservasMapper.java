package com.fernando.reservas.mappers;


import com.fernando.commons.dto.ReservasRequest;
import com.fernando.commons.dto.ReservasResponse;
import com.fernando.commons.enums.EstadoReserva;
import com.fernando.commons.mappers.CommonMapper;
import com.fernando.reservas.entities.Reserva;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ReservasMapper implements CommonMapper<ReservasRequest, ReservasResponse, Reserva>{


    @Override
    public Reserva requestAEntidad(ReservasRequest request) {
        if (request == null) return null;

        return Reserva.builder()
                .idHuesped(request.idHuesped())
                .idHabitacion(request.idHabitacion())
                .fechaEntrada(request.fechaEntrada())
                .fechaSalida(request.fechaSalida())
                .estadoReserva(EstadoReserva.CONFIRMADA)
                .build();
    }

    @Override
    public ReservasResponse entidadAResponse(Reserva entidad) {
        if (entidad == null) return null;

        return new ReservasResponse(
                entidad.getId(),
                entidad.getIdHuesped(),
                entidad.getIdHabitacion(),
                entidad.getFechaEntrada(),
                entidad.getFechaSalida(),
                entidad.getEstadoReserva().getDescripcion()
        );
    }
}
