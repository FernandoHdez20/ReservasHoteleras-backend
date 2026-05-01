package com.fernando.habitaciones.services;

import com.fernando.commons.dto.HabitacionRequest;
import com.fernando.commons.dto.HabitacionResponse;
import com.fernando.commons.enums.EstadoHabitacion;
import com.fernando.commons.services.CrudService;

public interface HabitacionService extends CrudService<HabitacionRequest, HabitacionResponse> {

    HabitacionResponse obtenerPorIdHabitacionSinEstado(Long id);

    void actualizarEstadoHabitacion(Long id, Long idEstadoHabitacion);

    void liberarHabitacion(Long id);


}
