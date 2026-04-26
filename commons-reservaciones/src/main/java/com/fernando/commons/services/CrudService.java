package com.fernando.commons.services;

import java.util.List;


public interface CrudService<RQ, RS>{
    List<RS> listar();

    RS obtenerPorId(long id);

    RS registrar(RQ request);

    RS actualizar(RQ request, long id);

    void eliminar(Long id);


}
