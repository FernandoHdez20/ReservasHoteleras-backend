package com.fernando.commons.enums;

import com.fernando.commons.exceptions.RecursoNoEncontradoException;
import com.fernando.commons.utils.StringCustomUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EstadoReserva {
	
	CONFIRMADA(1L, "Reserva creada"),
	EN_CURSO(2L,"Check-in realizado" ),
	FINALIZADA(3L,"Check-out realizado" ),
	CANCELADA(4L,"Reserva cancelada " );
	
	
	private final Long codigo;
	private final String descripcion;
	
	public static EstadoReserva obtenerEstadoReservaPorCodigo(Long codigo) {
		for(EstadoReserva e : values()) {
			if(e.codigo == codigo) {
				return e;
			}
		}
		throw new RecursoNoEncontradoException("Codigo de estado reserva no valido" + codigo);
		

	}
	
	public static EstadoReserva obtenerEstadoReservaPorDescripcion(String descripcion) {
		for(EstadoReserva e : values()) {
			String descEstadoReserva = StringCustomUtils.quitarAcentos(e.descripcion);			
					if(descEstadoReserva.equalsIgnoreCase(StringCustomUtils.quitarAcentos(descripcion))) {
				return e;
			}
		}
		throw new RecursoNoEncontradoException("descripcion de estado reserva no valido" + descripcion);
		

	}


}
