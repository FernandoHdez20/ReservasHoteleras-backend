package com.fernando.commons.enums;

import com.fernando.commons.exceptions.RecursoNoEncontradoException;
import com.fernando.commons.utils.StringCustomUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EstadoHabitacion {
	DISPONIBLE(1L, "Lista para asignarse"),
	OCUPADA(2L,"Asignada a una reserva" ),
	LIMPIEZA(3L,"En limpieza" ),
	MANTENIMIENTO(4L,"En reparación" );
	
	
	private final Long codigo;
	private final String descripcion;
	
	public static EstadoHabitacion obtenerEstadoHabitacionPorCodigo(Long codigo) {
		for(EstadoHabitacion e : values()) {
			if(e.codigo == codigo) {
				return e;
			}
		}
		throw new RecursoNoEncontradoException("Codigo de estado habitación no valido" + codigo);
		

	}
	
	public static EstadoHabitacion obtenerEstadoHabitacionPorDescripcion(String descripcion) {
		for(EstadoHabitacion e : values()) {
			String descEstadoHabitacion = StringCustomUtils.quitarAcentos(e.descripcion);			
					if(descEstadoHabitacion.equalsIgnoreCase(StringCustomUtils.quitarAcentos(descripcion))) {
				return e;
			}
		}
		throw new RecursoNoEncontradoException("descripcion de estado habitación no valido" + descripcion);
		

	}

	

}
