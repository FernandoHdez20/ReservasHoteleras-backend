package com.hotel.msvhuespedes.entities;

import com.fernando.commons.enums.EstadoRegistro;
import com.fernando.commons.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HUESPEDES")
@Builder
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HUESPED")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "TELEFONO", nullable = false, length = 10)
    private String telefono;

    @Column(name = "DOCUMENTO", nullable = false, length = 50)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_DOCUMENTO", nullable = false, length = 50)
    private TipoDocumento tipoDocumento;

    @Column(name = "NACIONALIDAD", nullable = false, length = 20)
    private String nacionalidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false,  length = 30)
    private EstadoRegistro estadoRegistro;

    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, String email, String telefono, String documento, TipoDocumento tipoDocumento, String nacionalidad) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
        this.telefono = telefono;
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.nacionalidad = nacionalidad;
    }

    public void eliminar(){
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }
}
