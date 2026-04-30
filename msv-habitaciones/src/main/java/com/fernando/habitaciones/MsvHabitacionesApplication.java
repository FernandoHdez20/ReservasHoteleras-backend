package com.fernando.habitaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvHabitacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvHabitacionesApplication.class, args);
	}

}
