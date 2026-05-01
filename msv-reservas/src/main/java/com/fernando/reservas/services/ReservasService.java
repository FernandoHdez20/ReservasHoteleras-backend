package com.fernando.reservas.services;

import com.fernando.commons.dto.ReservasRequest;
import com.fernando.commons.dto.ReservasResponse;
import com.fernando.commons.services.CrudService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public interface ReservasService extends CrudService<ReservasRequest, ReservasResponse> {
    //ReservasResponse actualizarEstado(Long idReserva, Long idEstado);

    void actualizarEstadoReservacion(Long idReservas, Long idEstadoReservacion);
    void huespedTieneReservacionesAsignadas(Long idAHuesped);
    void habitacionTieneReservacionAsignadas(Long idHabitacion);

}
