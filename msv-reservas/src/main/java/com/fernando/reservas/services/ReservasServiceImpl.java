package com.fernando.reservas.services;

import com.fernando.commons.clients.HabitacionClient;
import com.fernando.commons.clients.HuespedClient;
import com.fernando.commons.dto.HabitacionResponse;
import com.fernando.commons.dto.HuespedResponse;
import com.fernando.commons.dto.ReservasRequest;
import com.fernando.commons.dto.ReservasResponse;
import com.fernando.commons.enums.EstadoHabitacion;
import com.fernando.commons.enums.EstadoRegistro;
import com.fernando.commons.enums.EstadoReserva;
import com.fernando.commons.exceptions.EntidadRelacionadaException;
import com.fernando.commons.exceptions.RecursoNoEncontradoException;
import com.fernando.commons.services.CrudService;
import com.fernando.reservas.entities.Reserva;
import com.fernando.reservas.mappers.ReservasMapper;
import com.fernando.reservas.repositories.ReservasRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReservasServiceImpl implements ReservasService{

    private final ReservasRepository reservasRepository;
    private final ReservasMapper reservasMapper;
    private final HuespedClient huespedClient;
    private final HabitacionClient habitacionClient;
    private final List<EstadoReserva> ESTADOS_REGISTROS_ASIGNACION = List.of(EstadoReserva.CONFIRMADA, EstadoReserva.EN_CURSO);
    
    @Override
    @Transactional(readOnly = true)
    public List<ReservasResponse> listar() {
        log.info("Listando las reservaciones confirmadas");
        return reservasRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(reservacion ->
                        reservasMapper.entidadAResponse(
                                reservacion,
                                obtenerHuespedSinEstado(reservacion.getIdHuesped()),
                                obtenerHabitacionActivo(reservacion.getIdHabitacion())
                        )).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservasResponse obtenerPorId(Long id) {
        log.info("Buscando reservacion con id {}", id);
        Reserva reservas = obtenerReservacionOException(id);
        return  reservasMapper.entidadAResponse(reservas,
                obtenerHuespedSinEstado(reservas.getIdHuesped()),
                obtenerHabitacionSinEstado(reservas.getIdHabitacion()));
    }

    @Override
    public void huespedTieneReservacionesAsignadas(Long idAHuesped) {
        boolean tieneReservaciones = reservasRepository
                .existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(
                        idAHuesped,
                        EstadoRegistro.ACTIVO,
                        ESTADOS_REGISTROS_ASIGNACION);
        if (tieneReservaciones)
            throw new EntidadRelacionadaException(
                    "No se puede modicar el huesped ya que tiene reservacion  con estado: "
                            + ESTADOS_REGISTROS_ASIGNACION);
    }

    @Override
    public void habitacionTieneReservacionAsignadas(Long idHabitacion) {
        boolean tieneReservaciones = reservasRepository
                .existsByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(
                        idHabitacion,
                        EstadoRegistro.ACTIVO,
                        ESTADOS_REGISTROS_ASIGNACION);

        if (tieneReservaciones)
            throw new EntidadRelacionadaException(
                    "No se puede modicar el huesped ya que tiene reservacion  con estado: "
                            + ESTADOS_REGISTROS_ASIGNACION);
    }

    @Override
    public ReservasResponse registrar(ReservasRequest request) {
        log.info("Registrando nueva reserva... ");
        HuespedResponse huesped = obtenerHuespedActivo(request.idHuesped());
        HabitacionResponse habitacion = obtenerHabitacionActivo(request.idHabitacion());
        validarEstadoHabitacion(habitacion);

        validarHuespedTieneReservacionesAsignadas(request.idHuesped());
        validarHabitacionTieneReservacionesAsignadas(request.idHabitacion());
        validarFechaSalida(request);
        Reserva reservas = reservasRepository.save(reservasMapper.requestAEntidad(request));
        cambiarEstadoHbitacionSegunEstadoReserva(reservas.getIdHabitacion(),reservas.getEstadoReserva());

        return reservasMapper.entidadAResponse(reservas, huesped, habitacion);

    }

    @Override
    public void actualizarEstadoReservacion(Long idReservas, Long idEstadoReservacion) {
        Reserva reservas = obtenerReservacionOException(idReservas);
        log.info("Actualizando el estado de reservacion con id {} ", idReservas);

        EstadoReserva estadoReserva = EstadoReserva.obtenerEstadoReservaPorCodigo(idEstadoReservacion);

        reservas.actualizarEstadoReservacion(estadoReserva);

        cambiarEstadoHbitacionSegunEstadoReserva(reservas.getIdHabitacion(), reservas.getEstadoReserva());
        log.info("Estado de la reservacion {} actualizado correctamente ", reservas.getId());
    }

    @Override
    public ReservasResponse actualizar(ReservasRequest request, Long id) {
        Reserva reservas = obtenerReservacionOException(id);
        log.info("actualizando reservacion con id {} ", id);

        EstadoReserva estado = reservas.getEstadoReserva();

        if (estado == EstadoReserva.FINALIZADA || estado == EstadoReserva.CANCELADA)
            throw new IllegalStateException("No se puede actualizar una reservación " + estado);

        validarFechaSalida(request);

        if(reservas.getEstadoReserva() == EstadoReserva.EN_CURSO){

            if (!reservas.getIdHuesped().equals(request.idHuesped())
                    || !reservas.getIdHabitacion().equals(request.idHabitacion())
                    || !reservas.getFechaEntrada().equals(request.fechaEntrada()))
                throw new IllegalStateException(
                        "Una reservación EN_CURSO solo permite modificar la fecha de salida");

            reservas.actualizarFechas(
                    request.fechaSalida());

            return reservasMapper.entidadAResponse(reservas,
                    obtenerHuespedActivo(reservas.getIdHuesped()),
                    obtenerHabitacionActivo(reservas.getIdHabitacion()));
        }

        Long idHabitacionAnterior = reservas.getIdHabitacion();

        HuespedResponse huespedNuevo = reservas.getIdHuesped().equals(request.idHuesped())
                ? null : obtenerHuespedActivo(request.idHuesped());
        HabitacionResponse habitacionNueva = reservas.getIdHabitacion().equals(request.idHabitacion())
                ? null : obtenerHabitacionActivo(request.idHabitacion());

        if(huespedNuevo != null)
            validarHuespedTieneReservacionesAsignadas(request.idHuesped());

        if (habitacionNueva != null)
            validarEstadoHabitacion(habitacionNueva);

        reservas.actualizar(
                request.idHuesped(),
                request.idHabitacion(),
                request.fechaEntrada(),
                request.fechaSalida()
        );

        if (habitacionNueva != null) {
            cambiarEstadoHabitacion(idHabitacionAnterior,
                    EstadoHabitacion.DISPONIBLE.getCodigo());
            cambiarEstadoHbitacionSegunEstadoReserva(reservas.getIdHabitacion(),
                    reservas.getEstadoReserva());
        }
        log.info("Reservacion actualizada {} ", reservas.getId());

        return reservasMapper.entidadAResponse(reservas,
                obtenerHuespedActivo(reservas.getIdHuesped()),
                obtenerHabitacionActivo(reservas.getIdHabitacion()));
    }

    @Override
    public void eliminar(Long id) {
        Reserva reservas = obtenerReservacionOException(id);
        log.info("Eliminando la reservación con id {}", id);

        reservas.eliminar();

        if (reservas.getEstadoReserva() == EstadoReserva.CONFIRMADA) {
            cambiarEstadoHabitacion(
                    reservas.getIdHabitacion(),
                    EstadoHabitacion.DISPONIBLE.getCodigo()
            );
        }

        log.info("La reservación con id {} ha sido eliminada exitosamente", id);

    }


    private Reserva obtenerReservacionOException(Long id){
        log.info("Buscando reservacion con id {} ", id);
        return reservasRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(
                () -> new RecursoNoEncontradoException("Reservacion no encontrada con el id: " + id));
    }

    private HuespedResponse obtenerHuespedSinEstado(Long idHuesped){
        log.info("Listnado huespedes sin estado");
        return  huespedClient.obtenerPorIdTodos(idHuesped);
    }

    private HuespedResponse obtenerHuespedActivo(Long idHuesped){
        log.info("Listando huespedes activos");
        return  huespedClient.obtenerPorId(idHuesped);
    }

    private HabitacionResponse obtenerHabitacionSinEstado(Long idHabitacion){
        log.info("Listnado habitaciones sin estado");
        return  habitacionClient.obtenerHabitacionSinEstado(idHabitacion);
    }

    private HabitacionResponse obtenerHabitacionActivo(Long idHabitacion){
        log.info("Listando habitaciones activos");
        return  habitacionClient.obtenerHabitacionPorId(idHabitacion);
    }

    private void validarEstadoHabitacion(HabitacionResponse habitacion){
        log.info("Validando si la habitacion se encuentra en estado: {}", EstadoHabitacion.DISPONIBLE);
        if(!EstadoHabitacion.DISPONIBLE.getDescripcion().equalsIgnoreCase(habitacion.estadoHabitacion()))
            throw new IllegalStateException("La habitacion no se encuentra en estado: " + EstadoHabitacion.DISPONIBLE);
    }

    private void validarHuespedTieneReservacionesAsignadas(Long idHuesped){
        if (reservasRepository.existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(
                idHuesped, EstadoRegistro.ACTIVO, ESTADOS_REGISTROS_ASIGNACION))
            throw new IllegalArgumentException(
                    "No se puede reservar la habitacion ya que el huesped solo puede tener una reservacion activa con los estados: "
                            + ESTADOS_REGISTROS_ASIGNACION);
    }

    private void validarHabitacionTieneReservacionesAsignadas(Long idHabitacion){
        if (reservasRepository.existsByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(
                idHabitacion, EstadoRegistro.ACTIVO, ESTADOS_REGISTROS_ASIGNACION))
            throw new IllegalArgumentException(
                    "No se puede reservar la habitacion ya que el huesped solo puede tener una reservacion activa con los estados: "
                            + ESTADOS_REGISTROS_ASIGNACION);
    }

    private void cambiarEstadoHbitacionSegunEstadoReserva(Long idHabitacion, EstadoReserva EstadoReserva) {
        switch (EstadoReserva) {
            case CONFIRMADA, EN_CURSO ->
                    cambiarEstadoHabitacion(idHabitacion, EstadoHabitacion.OCUPADA.getCodigo());

            case FINALIZADA, CANCELADA ->
                    cambiarEstadoHabitacion(idHabitacion, EstadoHabitacion.DISPONIBLE.getCodigo());
        }
    }

    private void cambiarEstadoHabitacion(Long idHabitacion, Long idEstadoHabitacion){
        log.info("Actualizando estado de la habitacion con id {} a id {}",
                idHabitacion,EstadoHabitacion.obtenerEstadoHabitacionPorCodigo(idEstadoHabitacion));

        habitacionClient.actualizarEstadoHabitacion(idHabitacion, idEstadoHabitacion);
    }

    private void validarFechaSalida(ReservasRequest request){
        if (!request.fechaSalida().after(request.fechaEntrada()))
            throw new IllegalArgumentException(
                    "La fecha de salida debe ser mayor a la fecha de ingreso.");
    }
}
