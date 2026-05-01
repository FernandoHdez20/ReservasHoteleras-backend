package com.fernando.reservas.mappers;


import com.fernando.commons.dto.*;
import com.fernando.commons.enums.EstadoRegistro;
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
        if(request == null)return null;

        return Reserva.builder()
                .idHuesped(request.idHuesped())
                .idHabitacion(request.idHabitacion())
                .fechaEntrada(request.fechaEntrada())
                .fechaSalida(request.fechaSalida())
                .estadoReserva(EstadoReserva.CONFIRMADA)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public ReservasResponse entidadAResponse(Reserva entidad) {
        if (entidad == null) return null;

        return new ReservasResponse(
                entidad.getId(),
                null,
                null,
                entidad.getFechaEntrada(),
                entidad.getFechaSalida(),
                entidad.getEstadoReserva().getDescripcion());
    }

    public ReservasResponse entidadAResponse(Reserva entidad, HuespedResponse huesped, HabitacionResponse habitacion) {
        if (entidad == null) return null;

        return new ReservasResponse(
                entidad.getId(),
                this.huespedResponseADatosHuesped(huesped),
                this.habitacionResponseADatosHabitacion(habitacion),
                entidad.getFechaEntrada(),
                entidad.getFechaSalida(),
                entidad.getEstadoReserva().getDescripcion());
    }

    private DatosHuesped huespedResponseADatosHuesped(HuespedResponse huesped){
        if(huesped == null) return null;

        return new DatosHuesped(
                huesped.id(),
                huesped.nombre(),
                huesped.email(),
                huesped.telefono(),
                huesped.nacionalidad());
    }

    private DatosHabitacion habitacionResponseADatosHabitacion(HabitacionResponse habitacion){
        if(habitacion == null) return null;

        return new DatosHabitacion(
                habitacion.id(),
                habitacion.numero().toString(),
                habitacion.tipo(),
                habitacion.precio().toString(),
                habitacion.capacidad().toString());
    }
}
