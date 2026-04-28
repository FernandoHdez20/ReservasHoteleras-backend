package com.fernando.habitaciones.controllers;


import com.fernando.commons.controllers.CommonController;
import com.fernando.commons.dto.HabitacionRequest;
import com.fernando.commons.dto.HabitacionResponse;
import com.fernando.habitaciones.services.HabitacionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class habitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService> {


    public habitacionController(HabitacionService service) {
        super(service);
    }
}
