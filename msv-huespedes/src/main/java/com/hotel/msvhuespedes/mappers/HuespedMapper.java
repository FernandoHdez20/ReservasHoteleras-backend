package com.hotel.msvhuespedes.mappers;

import com.fernando.commons.dto.HuespedRequest;
import com.fernando.commons.dto.HuespedResponse;
import com.fernando.commons.enums.EstadoRegistro;
import com.hotel.msvhuespedes.entities.Huesped;
import org.springframework.stereotype.Component;

@Component
public class HuespedMapper {

    public Huesped requestAEntidad(HuespedRequest request){
        if(request == null) return null;

        return Huesped.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .email(request.email())
                .telefono(request.telefono())
                .documento(request.documento())
                .tipoDocumento(request.tipoDocumento())
                .nacionalidad(request.nacionalidad())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    public HuespedResponse  entidadAResponse(Huesped entidad){
        if(entidad == null) return null;

        return new HuespedResponse(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getApellidoPaterno(),
                entidad.getApellidoMaterno(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getDocumento(),
                entidad.getTipoDocumento(),
                entidad.getNacionalidad()
        );
    }
}
