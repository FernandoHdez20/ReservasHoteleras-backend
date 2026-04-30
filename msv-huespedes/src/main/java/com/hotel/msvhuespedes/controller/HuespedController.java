package com.hotel.msvhuespedes.controller;

import com.fernando.commons.controllers.CommonController;
import com.fernando.commons.dto.HuespedRequest;
import com.fernando.commons.dto.HuespedResponse;
import com.hotel.msvhuespedes.services.HuespedService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HuespedController extends CommonController<HuespedRequest, HuespedResponse, HuespedService> {

    public HuespedController(HuespedService service) {
        super(service);
    }

    @GetMapping("/id-huespedes/{id}")
    public ResponseEntity<HuespedResponse> obtenerHuespedTodos(
            @PathVariable @Positive(message = "El Id debe de ser posotivo") Long id){
        return ResponseEntity.ok(service.obtenerPorIdTodos(id));
    }
}
