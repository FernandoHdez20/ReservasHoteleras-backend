package com.hotel.msvhuespedes.services;

import com.fernando.commons.dto.HuespedRequest;
import com.fernando.commons.dto.HuespedResponse;
import com.fernando.commons.enums.EstadoRegistro;
import com.fernando.commons.exceptions.RecursoNoEncontradoException;
import com.hotel.msvhuespedes.entities.Huesped;
import com.hotel.msvhuespedes.mappers.HuespedMapper;
import com.hotel.msvhuespedes.repositories.HuespedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class HuespedServiceImpl implements HuespedService {

    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;

    @Override
    public List<HuespedResponse> listar() {
        log.info("Listando todos los huespedes activos");
        return huespedRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(huespedMapper::entidadAResponse).toList();
    }

    @Override
    public HuespedResponse obtenerPorId(long id) {
        return huespedMapper.entidadAResponse(obtenerHuespedActivoOException(id));
    }

    @Override
    public HuespedResponse obtenerPorIdTodos(Long id) {
        return huespedMapper.entidadAResponse(
                huespedRepository.findById(id).orElseThrow(
                        () -> new RecursoNoEncontradoException("Huesped no encontrado con id: " + id)));
    }

    @Override
    public HuespedResponse registrar(HuespedRequest request) {
        if (request == null) return null;
        log.info("Registrando nuevo huesped...");

        validarUnicidad(request);

        Huesped huesped = huespedMapper.requestAEntidad(request);

        huespedRepository.save(huesped);
        log.info("Paciente registrado con exito");
        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public HuespedResponse actualizar(HuespedRequest request, long id) {
        Huesped huesped = obtenerHuespedActivoOException(id);

        validarUnicidadActualizar(request, id);

        huesped.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono(),
                request.documento(),
                request.nacionalidad()
        );

        log.info("Huesped con id {} actualizado correctamente", id);
        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public void eliminar(Long id) {
        Huesped huesped = obtenerHuespedActivoOException(id);

        huespedTieneReservaciones(id);

        huesped.eliminar();
        log.info("Huesped con id {} ha sido eliminado", id);
    }

    private Huesped obtenerHuespedActivoOException(Long id) {
        log.info("Buscando Huesped con id: {}", id);
        return huespedRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(
                () -> new RecursoNoEncontradoException("Huesped no encontrado con id: " + id));
    }

    private void validarUnicidad(HuespedRequest request){

        if(huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un huesped registrado con el email: " + request.email());

        if(huespedRepository.existsByTelefonoAndEstadoRegistro(request.telefono(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un huesped registrado con el telefono: " + request.telefono());

        if(huespedRepository.existsByDocumentoIgnoreCaseAndEstadoRegistro(request.documento(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un huesped registrado con el documento: " + request.documento());
    }

    private void validarUnicidadActualizar(HuespedRequest request, Long id){

        if(huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
                request.email(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException(
                    "Ya existe un huesped registrado con el email: " + request.email());

        if(huespedRepository.existsByTelefonoAndEstadoRegistroAndIdNot(
                request.telefono(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException(
                    "Ya existe un huesped registrado con el telefono: " + request.telefono());

        if(huespedRepository.existsByDocumentoIgnoreCaseAndEstadoRegistroAndIdNot(
                request.documento(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException(
                    "Ya existe un huesped registrado con el documento: " + request.documento());
    }

    private void huespedTieneReservaciones(Long id) {
    }
}
