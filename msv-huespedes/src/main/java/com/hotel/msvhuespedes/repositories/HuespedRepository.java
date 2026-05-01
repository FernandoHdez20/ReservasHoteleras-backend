package com.hotel.msvhuespedes.repositories;

import com.fernando.commons.enums.EstadoRegistro;
import com.hotel.msvhuespedes.entities.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {

    List<Huesped> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Huesped> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estado);

    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estado);

    boolean existsByDocumentoIgnoreCaseAndEstadoRegistro(String documento, EstadoRegistro estado);

    boolean existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
            String email, EstadoRegistro estado, Long id);

    boolean existsByTelefonoAndEstadoRegistroAndIdNot(
            String telefono, EstadoRegistro estado, Long id);

    boolean existsByDocumentoIgnoreCaseAndEstadoRegistroAndIdNot(
            String documento, EstadoRegistro estado, Long id);
}
