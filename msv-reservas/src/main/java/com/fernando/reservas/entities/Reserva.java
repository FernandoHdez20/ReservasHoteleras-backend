package com.fernando.reservas.entities;

import com.fernando.commons.enums.EstadoRegistro;
import com.fernando.commons.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "RESERVAS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long id;

    @Column(name = "ID_HUESPED")
    private Long idHuesped;

    @Column(name = "ID_HABITACION")
    private Long idHabitacion;

    @Column(name = "FECHA_ENTRADA")
    private Date fechaEntrada;

    @Column(name = "FECHA_SALIDA")
    private  Date fechaSalida;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_RESERVA")
    private EstadoReserva estadoReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO")
    private EstadoRegistro estadoRegistro;


    public void cancelar() {
        if (this.estadoReserva != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se pueden cancelar reservas en estado CONFIRMADA");
        }
        this.estadoReserva = EstadoReserva.CANCELADA;
    }


    public void eliminar() {
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }


    public void checkIn() {
        if (this.estadoReserva != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo reservas confirmadas pueden hacer check-in");
        }
        this.estadoReserva = EstadoReserva.EN_CURSO;
    }

    public void checkOut() {
        if (this.estadoReserva != EstadoReserva.EN_CURSO) {
            throw new IllegalStateException(
                    "Solo reservas en estado EN_CURSO pueden hacer check-out"
            );
        }
        this.estadoReserva = EstadoReserva.FINALIZADA;
    }

    public void actualizar(Long idHuesped, Long idHabitacion, Date fechaEntrada, Date fechaSalida) {
        this.idHuesped = idHuesped;
        this.idHabitacion = idHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    public void actualizarFechas(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }


    public void actualizarEstadoReservacion(EstadoReserva nuevoEstado) {
        switch (this.estadoReserva) {
            case CONFIRMADA -> {
                if (!(nuevoEstado.equals(EstadoReserva.EN_CURSO)
                        || nuevoEstado.equals(EstadoReserva.CANCELADA)
                        || nuevoEstado.equals(EstadoReserva.CONFIRMADA)))
                    throw new IllegalStateException(
                            "La reservación con estado " + this.estadoReserva
                                    + " solo puede cambiar a " + EstadoReserva.EN_CURSO
                                    + " o " + estadoReserva.CANCELADA);
            }
            case EN_CURSO -> {
                if (!(nuevoEstado.equals(EstadoReserva.FINALIZADA)
                        || nuevoEstado.equals(EstadoReserva.EN_CURSO)))
                    throw new IllegalStateException(
                            "La reservación con estado " + this.estadoReserva
                                    + " solo puede cambiar a " + EstadoReserva.FINALIZADA);
            }
            case FINALIZADA, CANCELADA -> {
                if (!nuevoEstado.equals(this.estadoReserva))
                    throw new IllegalStateException(
                            "La reservación en estado " + this.estadoReserva
                                    + " no puede cambiar de estado");
            }
        }
        this.estadoReserva = nuevoEstado;
    }

}


