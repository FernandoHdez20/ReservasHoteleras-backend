package com.fernando.habitaciones.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ValueGenerationType;

@Entity
@Table(name = "HABITACIONES")
public class Habitaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION")
    private Long abitacion;

    @Column(name = "TIPO")
    private String tipo;



}

