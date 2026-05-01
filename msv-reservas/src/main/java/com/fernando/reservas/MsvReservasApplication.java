package com.fernando.reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.fernando.reservas","com.fernando.commons" })
public class MsvReservasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvReservasApplication.class, args);
    }

}
