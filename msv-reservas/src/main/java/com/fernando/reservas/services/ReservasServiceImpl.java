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
    private final HabitacionClient habitacionClient;
    private final HuespedClient huespedClient;
    @Override
    @Transactional(readOnly = true)
    public List<ReservasResponse> listar() {
        return reservasRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(reservasMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservasResponse obtenerPorId(Long id) {
        return reservasMapper.entidadAResponse(obtenerReservaOException(id));
    }

    @Override
    public ReservasResponse registrar(ReservasRequest request) {
        log.info("Registrando nueva reserva...");

        // 1. Validar fechas
        validarFechas(request);

        // 2. Validar que el huésped existe y está ACTIVO
        validarHuespedActivo(request.idHuesped());

        // 3. Validar habitación existe y está DISPONIBLE
        validarHabitacionDisponible(request.idHabitacion());

        // 4. Mapear y establecer estados iniciales
        Reserva reserva = reservasMapper.requestAEntidad(request);
        reserva.setEstadoReserva(EstadoReserva.CONFIRMADA);
        reserva.setEstadoRegistro(EstadoRegistro.ACTIVO);

        reservasRepository.save(reserva);

        // 5. Cambiar habitación a OCUPADA
        ocuparHabitacion(request.idHabitacion());

        log.info("Reserva registrada y habitación ocupada");
        return reservasMapper.entidadAResponse(reserva);

    }

    @Override
    public ReservasResponse actualizar(ReservasRequest request, Long id) {
        Reserva reserva = obtenerReservaOException(id);
        log.info("Actualizando reserva con id: {}", id);

        //No permitir cambio de habitación (regla global)
        if (request.idHabitacion() != null &&
                !request.idHabitacion().equals(reserva.getIdHabitacion())) {
            throw new IllegalStateException("No se permite cambiar la habitación en una reserva existente");
        }

        // Validar según estado
        validarActualizacionSegunEstado(reserva, request);

        // Guardar cambios
        reservasRepository.save(reserva);

        return reservasMapper.entidadAResponse(reserva);
    }

    @Override
    public void eliminar(Long id) {
        // cancelar la reserva
        log.info("Cancelando reserva con id: {}", id);

        Reserva reserva = obtenerReservaOException(id);

        reserva.cancelar();
        reserva.eliminar();

        liberarHabitacion(reserva.getIdHabitacion());

        log.info("Reserva {} cancelada correctamente", id);

    }

    public ReservasResponse actualizarEstado(Long idReserva, Long idEstado) {
        Reserva reserva = obtenerReservaOException(idReserva);
        EstadoReserva nuevoEstado = EstadoReserva.obtenerEstadoReservaPorCodigo(idEstado);

        log.info("Actualizando estado de reserva {} a {}", idReserva, nuevoEstado);

        switch (nuevoEstado) {
            case EN_CURSO -> reserva.checkIn();

            case FINALIZADA -> {
                reserva.checkOut();
                liberarHabitacion(reserva.getIdHabitacion());
            }

            case CANCELADA -> {
                reserva.cancelar();
                liberarHabitacion(reserva.getIdHabitacion());
            }

            case CONFIRMADA -> throw new IllegalStateException("No se puede regresar a CONFIRMADA");
        }

        return reservasMapper.entidadAResponse(reserva);
    }

    public ReservasResponse checkIn(Long id) {
        Reserva reserva = obtenerReservaOException(id);
        reserva.checkIn();
        log.info("Check-in realizado para reserva {}", id);
        return reservasMapper.entidadAResponse(reserva);
    }

    public ReservasResponse checkOut(Long id) {
        Reserva reserva = obtenerReservaOException(id);
        reserva.checkOut();
        liberarHabitacion(reserva.getIdHabitacion());
        log.info("Check-out realizado y habitación liberada para reserva {}", id);
        return reservasMapper.entidadAResponse(reserva);
    }

    private Reserva obtenerReservaOException(Long id) {
        log.info("Buscando reserva con id {}", id);
        return reservasRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Reserva no encontrada con id " + id));
    }

    private void validarFechas(ReservasRequest request) {
        log.info("Validando fechas de reserva...");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date hoy = cal.getTime();

        if (request.fechaEntrada().before(hoy)) {
            throw new IllegalArgumentException("La fecha de entrada no puede ser en el pasado");
        }

        if (!request.fechaEntrada().before(request.fechaSalida())) {
            throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la de salida");
        }
    }

    //valida que el huésped exista y esté ACTIVO
    private void validarHuespedActivo(Long idHuesped) {
        log.info("Validando huésped con id {}", idHuesped);

        HuespedResponse huesped = huespedClient.obtenerPorId(idHuesped);

        if (huesped == null) {
            throw new RecursoNoEncontradoException("El huésped no existe con id " + idHuesped);
        }

        if (!huesped.estadoRegistro().equalsIgnoreCase("ACTIVO")) {
            throw new IllegalStateException("El huésped no está activo");
        }
    }

    private void validarHabitacionDisponible(Long idHabitacion) {
        HabitacionResponse habitacion;
        try {
            habitacion = habitacionClient.obtenerHabitacion(idHabitacion);
        } catch (Exception e) {
            throw new RecursoNoEncontradoException("La habitación no existe con id " + idHabitacion);
        }

        if (!habitacion.estadoHabitacion().equalsIgnoreCase("DISPONIBLE") ||
                !habitacion.estadoRegistro().equalsIgnoreCase("ACTIVO")) {
            throw new IllegalStateException("La habitación no está disponible o activa");
        }
    }

    private void ocuparHabitacion(Long idHabitacion) {
        log.info("Marcando habitación {} como OCUPADA", idHabitacion);
        habitacionClient.actualizarEstadoHabitacion(idHabitacion, EstadoHabitacion.OCUPADA.getCodigo());
    }

    private void liberarHabitacion(Long idHabitacion) {
        log.info("Marcando habitación {} como DISPONIBLE", idHabitacion);
        habitacionClient.actualizarEstadoHabitacion(idHabitacion, EstadoHabitacion.DISPONIBLE.getCodigo());
    }

    private HuespedResponse obtenerHuespedActivo(Long id) {
        log.info("Buscando médico activo con id {} en el servicio remoto", id);
        return huespedClient.obtenerPorId(id);

    }

    private HuespedResponse obtenerHuespedSinEstado(Long id) {
        log.info("Buscando médico sin estado con id {} en el servicio remoto", id);
        return huespedClient.obtenerPorIdTodos(id);

    }

    private void validarActualizacionSegunEstado(Reserva reserva, ReservasRequest request) {

        switch (reserva.getEstadoReserva()) {

            case CONFIRMADA -> {
                //Se pueden modificar ambas fechas
                validarFechas(request);

                reserva.setFechaEntrada(request.fechaEntrada());
                reserva.setFechaSalida(request.fechaSalida());
            }

            case EN_CURSO -> {

                // No permitir modificar fecha de entrada
                if (request.fechaEntrada() != null &&
                        !request.fechaEntrada().equals(reserva.getFechaEntrada())) {
                    throw new IllegalStateException(
                            "No se puede modificar la fecha de entrada en una reserva EN_CURSO"
                    );
                }

                //Validar que venga fecha de salida
                if (request.fechaSalida() == null) {
                    throw new IllegalArgumentException("La fecha de salida es requerida");
                }

                // Validar coherencia de fechas
                if (!reserva.getFechaEntrada().before(request.fechaSalida())) {
                    throw new IllegalArgumentException(
                            "La fecha de salida debe ser posterior a la fecha de entrada"
                    );
                }

                reserva.setFechaSalida(request.fechaSalida());
            }

            case FINALIZADA, CANCELADA ->
                    throw new IllegalStateException(
                            "No se puede modificar una reserva en estado " + reserva.getEstadoReserva()
                    );
        }
    }
}
