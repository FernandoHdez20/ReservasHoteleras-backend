package com.fernando.reservas.controllers;

import com.fernando.commons.controllers.CommonController;
import com.fernando.commons.dto.ReservasRequest;
import com.fernando.commons.dto.ReservasResponse;
import com.fernando.reservas.services.ReservasService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ReservasController extends CommonController<ReservasRequest, ReservasResponse, ReservasService> {

    public ReservasController(ReservasService service) {

        super(service);
    }

    @PatchMapping("/{idReserva}/estado/{idEstado}")
    public ResponseEntity<ReservasResponse> actualizarEstado(
            @PathVariable Long idReserva,
            @PathVariable Long idEstado) {
        return ResponseEntity.ok(service.actualizarEstado(idReserva, idEstado));
    }
}
