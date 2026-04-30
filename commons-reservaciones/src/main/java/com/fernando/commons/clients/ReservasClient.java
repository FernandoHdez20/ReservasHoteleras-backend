package com.fernando.commons.clients;

import com.fernando.commons.dto.HabitacionResponse;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "msv-reservas")
public interface ReservasClient {


}
