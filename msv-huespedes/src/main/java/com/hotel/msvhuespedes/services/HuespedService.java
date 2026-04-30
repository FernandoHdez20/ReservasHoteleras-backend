package com.hotel.msvhuespedes.services;

import com.fernando.commons.dto.HuespedRequest;
import com.fernando.commons.dto.HuespedResponse;
import com.fernando.commons.services.CrudService;
import com.hotel.msvhuespedes.entities.Huesped;

public interface HuespedService extends CrudService<HuespedRequest, HuespedResponse> {

    HuespedResponse obtenerPorIdTodos(Long id);
}
