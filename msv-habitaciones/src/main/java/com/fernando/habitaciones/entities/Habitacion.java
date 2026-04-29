package com.fernando.habitaciones.entities;

import com.fernando.commons.enums.EstadoHabitacion;
import com.fernando.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "HABITACION")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION", nullable = false)
    private Long id;

    @Column(name = "NUMERO", nullable = false)
    private Integer numero;

    @Column(name = "TIPO", nullable = false)
    private String tipo;

    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_HABITACION", nullable = false)
    private EstadoHabitacion estadoHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;


    public void actualizar(Integer numero, String tipo, BigDecimal precio, Integer capacidad, EstadoHabitacion estadoHabitacion) {
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.capacidad = capacidad;
        this.estadoHabitacion = estadoHabitacion;
    }

    public void eliminar() {
        if (this.estadoHabitacion == EstadoHabitacion.OCUPADA){
            throw new IllegalArgumentException("No se puede eliminar una habitación ocupada");
        }

        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    public void actualizarEstadoHabitacion(EstadoHabitacion nuevoEstado) {

        if (this.estadoHabitacion == EstadoHabitacion.OCUPADA &&
                nuevoEstado != EstadoHabitacion.OCUPADA) {

            throw new IllegalArgumentException(
                    "Una habitación ocupada no puede cambiar de estado manualmente"
            );
        }

        this.estadoHabitacion = nuevoEstado;
    }
}



