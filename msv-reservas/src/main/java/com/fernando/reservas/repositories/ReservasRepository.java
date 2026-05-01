package com.fernando.reservas.repositories;

import com.fernando.commons.enums.EstadoRegistro;
import com.fernando.commons.enums.EstadoReserva;
import com.fernando.reservas.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface ReservasRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstadoRegistro(EstadoRegistro estadoRegistro);


    Optional<Reserva> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(Long idHuesped, EstadoRegistro estadoRegistro, Collection<EstadoReserva> estadoReserva);

    boolean existsByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(Long idHabitacion, EstadoRegistro estadoRegistro, Collection<EstadoReserva> estadoReserva);

}
