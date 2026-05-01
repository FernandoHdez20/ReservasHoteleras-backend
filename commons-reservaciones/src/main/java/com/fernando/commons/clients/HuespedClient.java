package com.fernando.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


import com.fernando.commons.dto.HuespedResponse;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "msv-huespedes")
public interface HuespedClient {

    @GetMapping("/huespedes/{idHuesped}")
    HuespedResponse obtenerPorId(@PathVariable Long idHuesped);

    @GetMapping("/huespedes/id-huespedes/{id}")
    HuespedResponse obtenerPorIdTodos(@PathVariable Long id);
}
