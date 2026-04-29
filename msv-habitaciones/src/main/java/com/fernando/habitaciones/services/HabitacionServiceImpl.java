package com.fernando.habitaciones.services;

import com.fernando.commons.dto.HabitacionRequest;
import com.fernando.commons.dto.HabitacionResponse;
import com.fernando.commons.enums.EstadoHabitacion;
import com.fernando.commons.enums.EstadoRegistro;
import com.fernando.commons.exceptions.RecursoNoEncontradoException;
import com.fernando.habitaciones.entities.Habitacion;
import com.fernando.habitaciones.mappers.HabitacionMapper;
import com.fernando.habitaciones.repositories.HabitacionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HabitacionServiceImpl implements HabitacionService{

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        return habitacionRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(habitacionMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorId(Long id) {
        return habitacionMapper.entidadAResponse(obtenerHabitacionActivaOException(id));
    }

    @Override
    public HabitacionResponse obtenerPorIdHabitacionSinEstado(Long id) {
        return habitacionMapper.entidadAResponse(habitacionRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontradoException("Habitacion sin estado no encontrado con el id: " + id)));
    }


    @Override
    public void actualizarEstadoHabitacion(Long id, Long idEstadoHabitacion) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);

        log.info("Actualizando estado de la habitación: {}", habitacion.getId());

        EstadoHabitacion estadoHabitacion = EstadoHabitacion.obtenerEstadoHabitacionPorCodigo(idEstadoHabitacion);

        habitacion.actualizarEstadoHabitacion(estadoHabitacion);

        log.info("Estado de la habitación {} actualizado correctamente",habitacion.getId());

    }

    @Override
    public HabitacionResponse registrar(HabitacionRequest request) {
        log.info("Registrando nueva habitacion...");

        validarNumeroUnico(request.numero());

        Habitacion habitacion = habitacionMapper.requestAEntidad(request);

        habitacionRepository.save(habitacion);

        log.info("Habitacion registrada con exito {}", habitacion.getNumero());

        return habitacionMapper.entidadAResponse(habitacion);

    }

    @Override
    public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);


        validarActualizarUnicos(request, EstadoRegistro.ACTIVO, id);

        habitacion.actualizar(
                request.numero(),
                request.tipo(),
                request.precio(),
                request.capacidad(),
                habitacion.getEstadoHabitacion()
        );

        log.info("Actualizando habitacion con id: {}", id);

        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando habitacion con id: {}", id);
        Habitacion habitacion = obtenerHabitacionActivaOException(id);

        habitacion.eliminar();
        log.info("Habitación con id {} ha sido eliminada", id);

    }

    private Habitacion obtenerHabitacionOException(Long id) {
        log.info("Buscando habitación con id {}", id);
        return habitacionRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Habitacion no encontrada con el id " + id));
    }

    private Habitacion obtenerHabitacionActivaOException(Long id) {
        log.info("Buscando Habitacion con id: {}, estadoRegistro.ACTIVO", id);
        return habitacionRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() ->
                new RecursoNoEncontradoException("Habitacion Activa no encontrada con el id " + id));
    }

    private void validarNumeroUnico(Integer numero) {
        log.info("Validando número de habitacion único...");
        if (habitacionRepository.existsByNumeroAndEstadoRegistro(numero,EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe una habitacion registrada con el número: " + numero);
        }
    }

    private void validarActualizarUnicos(HabitacionRequest request, EstadoRegistro estadoRegistro, Long id) {
         if (habitacionRepository.existsByNumeroAndEstadoRegistroAndIdNot(request.numero(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe una habitacion registrada con el número: " + request.numero());
    }


}
